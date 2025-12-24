const app = getApp();

Page({
  data: {
    userInfo: null,
    isVip: false
  },

  onShow() {
    this.checkUserStatus();
  },

  // 检查用户状态
  checkUserStatus() {
    const user = wx.getStorageSync('userInfo');
    if (user) {
      // 简单处理一下日期格式，只取前10位 (2025-12-12)
      if(user.vipExpireTime && user.vipExpireTime.length > 10) {
        user.vipExpireTime = user.vipExpireTime.substring(0, 10);
      }

      this.setData({ 
        userInfo: user,
        isVip: user.isVip === 1 
      });
    } else {
      this.setData({ userInfo: null, isVip: false });
    }
  },

  // 跳转去登录 (WXML 里点击头部触发)
  goLogin() {
    wx.navigateTo({
      url: '/pages/login/login',
    });
  },

  // 点击开通会员
  handleBuyVip(e) {
    // 1. 没登录先去登录
    if (!this.data.userInfo) {
      wx.showToast({ title: '请先登录', icon: 'none' });
      setTimeout(() => { this.goLogin(); }, 1000);
      return;
    }

    const type = e.currentTarget.dataset.type; // 1=月卡, 2=季卡

    wx.showLoading({ title: '正在开通...' });

    wx.request({
      url: 'http://localhost:8080/user/buyVip',
      method: 'POST',
      header: { 'content-type': 'application/x-www-form-urlencoded' },
      data: {
        userId: this.data.userInfo.id,
        type: type
      },
      success: (res) => {
        wx.hideLoading();
        if (res.data.code === 1) {
          wx.showToast({ title: '开通成功！', icon: 'success' });

          // 更新本地缓存
          const newUser = res.data.data;
          wx.setStorageSync('userInfo', newUser);
          
          // 刷新页面
          this.checkUserStatus();
        } else {
          wx.showToast({ title: res.data.msg || '开通失败', icon: 'none' });
        }
      },
      fail: () => {
        wx.hideLoading();
        wx.showToast({ title: '网络连接失败', icon: 'none' });
      }
    });
  }
})