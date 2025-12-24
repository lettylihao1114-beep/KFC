// app.js
App({
  onLaunch: function () {
    // 1. 云开发初始化 (保留原有逻辑)
    if (!wx.cloud) {
      console.error('请使用 2.2.3 或以上的基础库以使用云能力')
    } else {
      wx.cloud.init({
        traceUser: true,
      })
    }

    // 2. 定义全局变量
    this.globalData = {
      userInfo: null, // 微信用户信息
      user: null,     // 后端返回的数据库用户信息
      shop: null,     // 当前店铺信息
      baseUrl: 'http://localhost:8080' // 后端接口地址
    }

    // 3. 启动时自动模拟登录
    this.login();
  },

  // 登录逻辑
  login() {
    const that = this;
    // 模拟登录 (userId=1，这里写死是为了方便实训演示)
    wx.request({
      url: `${this.globalData.baseUrl}/user/login?userId=1`,
      method: 'GET',
      success(res) {
        // ✨✨✨ 关键修复：兼容处理 ✨✨✨
        // 后端可能返回标准的 R 对象 (code=1, data=User)，也可能直接返回 User 对象
        let userData = null;
        
        // 情况 A: 标准 R 对象
        if (res.data && res.data.code === 1) {
            userData = res.data.data;
        } 
        // 情况 B: 直接返回 User 对象 (兼容旧接口)
        else if (res.data && res.data.id) {
            userData = res.data;
        }

        // 如果成功获取到用户数据
        if (userData) {
          console.log('✅ App自动登录成功:', userData);
          that.globalData.user = userData;
          
          // ✨✨✨ 核心修复：把 Token 存入本地缓存 ✨✨✨
          // 拦截器(LoginInterceptor)需要这个 token 才能放行
          const token = userData.token || userData.id; 
          wx.setStorageSync('token', token); 
          
          if (userData.isVip === 1) {
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