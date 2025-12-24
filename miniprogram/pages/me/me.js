const app = getApp();

Page({
  data: {
    userInfo: null,
    isVip: false,      // 用于控制 WXML 里的黑金卡片样式
    vipExpireText: '', // 用于显示有效期
    vouchers: []       // 优惠券列表
  },

  /**
   * 每次页面显示时触发
   * 必须在这里读取缓存，确保在"开通会员"页操作后回来能立即刷新
   */
  onShow() {
    this.checkLogin();
  },

  checkLogin() {
    // 1. 从缓存读取最新用户信息 (注意 key 是 'userInfo')
    const user = wx.getStorageSync('userInfo');
    
    if (user) {
      // 同步到全局
      app.globalData.user = user;

      // 2. 处理 VIP 有效期显示 (截取 T 之前的部分 2025-12-12)
      let expireText = '';
      if (user.vipExpireTime && user.vipExpireTime.length > 10) {
        expireText = user.vipExpireTime.substring(0, 10); // 或者用 split('T')[0]
      }

      // 3. 更新页面数据
      this.setData({ 
        userInfo: user, 
        isVip: user.isVip === 1,
        vipExpireText: expireText 
      });

      // 4. 获取优惠券
      this.fetchVouchers(user.id);
    } else {
      // 未登录，清空数据
      this.setData({ userInfo: null, isVip: false, vouchers: [], vipExpireText: '' });
    }
  },

  // 跳转登录页
  goLogin() {
    wx.navigateTo({
      url: '/pages/login/login'
    });
  },

  // 退出登录
  handleLogout() {
    // 1. 清空全局数据
    app.globalData.user = null;
    
    // 2. 清空本地缓存 (关键：key 要和登录时存的一致)
    wx.removeStorageSync('userInfo'); 
    wx.removeStorageSync('token');

    // 3. 重置页面数据
    this.setData({ 
      userInfo: null, 
      isVip: false,
      vouchers: [], 
      vipExpireText: '' 
    });
    
    wx.showToast({ title: '已退出', icon: 'none' });
    
    // 退出后延迟跳转去登录页，体验更好
    setTimeout(() => {
        wx.navigateTo({ url: '/pages/login/login' });
    }, 1000);
  },

  // === 页面跳转逻辑 ===

  goToAddress() {
    if (!this.data.userInfo) return wx.showToast({ title: '请先登录', icon: 'none' });
    wx.navigateTo({ url: '/pages/address/address' });
  },

  goToOrders() {
    if (!this.data.userInfo) return wx.showToast({ title: '请先登录', icon: 'none' });
    wx.navigateTo({ url: '/pages/orders/orders' });
  },

  // 跳转到开通会员页
  goVip() {
    wx.switchTab({ url: '/pages/vip/vip' });
  },

  // 跳转到商家后台登录
  goToAdminLogin() {
    wx.navigateTo({ url: '/pages/admin-login/admin-login' });
  },

  // === 核心业务：获取卡券 ===
  fetchVouchers(userId) {
    const that = this;
    // 使用全局 baseUrl，如果没配则默认 localhost
    const baseUrl = app.globalData.baseUrl || 'http://localhost:8080';
    
    wx.request({
      url: `${baseUrl}/user/voucher/list?userId=${userId}`,
      method: 'GET',
      success(res) {
        // 兼容处理：后端可能返回 R 对象，也可能直接返回 List
        if (res.statusCode === 200) {
            // 情况 A: 返回标准 R 对象 (code === 1)
            if (res.data.code === 1) {
                that.setData({ vouchers: res.data.data });
            } 
            // 情况 B: 旧接口直接返回数组
            else if (Array.isArray(res.data)) {
                that.setData({ vouchers: res.data });
            }
        }
      },
      fail(err) {
          console.error('获取卡券失败', err);
      }
    });
  }
});