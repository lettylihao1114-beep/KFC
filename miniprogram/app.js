// app.js
App({
  onLaunch: function () {
    // 云开发初始化 (保留原样，防止报错)
    if (!wx.cloud) {
      console.error('请使用 2.2.3 或以上的基础库以使用云能力')
    } else {
      wx.cloud.init({
        traceUser: true,
      })
    }

    // 👇 核心修改：增加了 baseUrl 和 shop
    this.globalData = {
      userInfo: null,
      user: null,      // 存后端返回的用户对象 (id, isVip, balance...)
      shop: null,      // 存店铺信息 (id, name, status...)
      baseUrl: 'http://localhost:8080' // 👈 统一接口地址，方便后续调用
    }

    // 自动登录
    this.login();
  },

  login() {
    const that = this;
    // 模拟登录 (userId=1)
    // 使用模板字符串拼接 baseUrl
    wx.request({
      url: `${this.globalData.baseUrl}/user/login?userId=1`,
      method: 'GET',
      success(res) {
        if (res.statusCode === 200 && res.data) {
          console.log('✅ App自动登录成功:', res.data);
          that.globalData.user = res.data;
          
          // 如果当前用户是 VIP，可以在这里打印一下，方便调试
          if (res.data.isVip === 1) {
            console.log('👑 尊贵的大神卡用户');
          }
        }
      },
      fail(err) {
        console.error('❌ 登录失败 (请检查后端是否启动)', err);
      }
    });
  }
})