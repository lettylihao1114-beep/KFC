const app = getApp();

Page({
  data: {
    isAgreed: false // 协议勾选状态
  },

  // 勾选/取消协议
  toggleAgree() {
    this.setData({
      isAgreed: !this.data.isAgreed
    });
  },

  // 1. 一键登录 (演示版：直接登录 test01)
  handleOneClickLogin() {
    if (!this.data.isAgreed) {
      return wx.showToast({ title: '请先同意协议', icon: 'none' });
    }

    wx.showLoading({ title: '极速登录中...' });
    
    // 复用之前的 test01 登录逻辑
    wx.request({
      url: 'http://localhost:8080/user/login',
      method: 'POST',
      data: { username: 'test01', password: '123' },
      success: (res) => {
        wx.hideLoading();
        if (res.data.code === 1) {
          app.globalData.user = res.data.data;
          wx.setStorageSync('userInfo', res.data.data);
          wx.showToast({ title: '登录成功' });
          
          // 延迟跳转
          setTimeout(() => { 
             wx.switchTab({ 
               url: '/pages/index/index',
               fail: () => { wx.reLaunch({ url: '/pages/index/index' }) } 
             }) 
          }, 1000);
        } else {
          wx.showToast({ title: '登录失败，请尝试密码登录', icon: 'none' });
        }
      },
      fail: () => { 
        wx.hideLoading(); 
        wx.showToast({ title: '服务器连接失败', icon: 'none' }); 
      }
    });
  },

  // 2. 跳转到手机验证码页
  goToMobileLogin() {
    wx.navigateTo({ url: '/pages/login-code/login-code' });
  },

  // 3. 跳转到账号密码登录页
  goToPasswordLogin() {
    wx.navigateTo({ url: '/pages/login-password/login-password' });
  },

  // === 4. 关键修复：跳转到注册页 ===
  goToRegister() {
    console.log("准备跳转注册页..."); // 调试日志
    wx.navigateTo({
      url: '/pages/register/register',
      fail: (err) => {
        console.error("跳转失败，请检查 app.json 是否注册了该页面", err);
        wx.showToast({ title: '页面跳转失败', icon: 'none' });
      }
    });
  }
})