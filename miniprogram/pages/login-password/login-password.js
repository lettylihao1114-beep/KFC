const app = getApp();

Page({
  data: {
    username: '',
    password: '',
    canLogin: false // 用于控制按钮状态
  },

  // 输入账号
  onUsernameInput(e) {
    this.setData({ username: e.detail.value });
    this.checkLoginStatus();
  },

  // 输入密码
  onPasswordInput(e) {
    this.setData({ password: e.detail.value });
    this.checkLoginStatus();
  },

  // 检查是否都输入了
  checkLoginStatus() {
    const { username, password } = this.data;
    // 去掉空格后的长度大于0
    const canLogin = username.trim().length > 0 && password.trim().length > 0;
    this.setData({ canLogin });
  },

  // 点击登录
  handleLogin() {
    if (!this.data.canLogin) return;

    wx.showLoading({ title: '验证身份...' });

    // === 核心修改：顾客登录逻辑 ===
    wx.request({
      // 1. 接口改为顾客登录
      url: 'http://localhost:8080/user/login', 
      method: 'POST',
      data: {
        username: this.data.username,
        password: this.data.password
      },
      success: (res) => {
        wx.hideLoading();
        console.log("顾客登录返回：", res.data);

        // 2. 判断后端返回码 (code=1 为成功)
        if (res.data.code === 1) {
          
          // 3. 拿到用户信息并保存
          const userInfo = res.data.data;
          app.globalData.user = userInfo;
          wx.setStorageSync('userInfo', userInfo);

          wx.showToast({ title: '欢迎回来', icon: 'success' });
          
          // 4. 延迟跳转 (万能兼容版)
          setTimeout(() => {
             console.log("准备跳转首页...");
             
             // 优先尝试 switchTab (针对 TabBar 页面)
             wx.switchTab({
               url: '/pages/index/index',
               success: () => console.log("switchTab 跳转成功"),
               fail: (err) => {
                 console.warn("switchTab 失败，尝试 reLaunch", err);
                 
                 // 如果失败 (说明不是 TabBar 页面)，尝试强制重启跳转
                 wx.reLaunch({
                   url: '/pages/index/index',
                   fail: (e) => {
                     console.error("所有跳转方式都失败了，请检查路径！", e);
                     wx.showModal({ title: '跳转失败', content: '找不到首页 /pages/index/index' });
                   }
                 });
               }
             });
          }, 1000);

        } else {
          // 登录失败
          wx.showToast({ 
            title: res.data.msg || '账号或密码错误', 
            icon: 'none' 
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        console.error("顾客登录网络错误", err);
        wx.showToast({ title: '服务器连接失败', icon: 'none' });
      }
    });
  }
})