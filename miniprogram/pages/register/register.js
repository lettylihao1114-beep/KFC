const app = getApp();

Page({
  data: {
    username: '',
    phone: '',
    password: '',
    confirmPassword: ''
  },

  // === 1. 输入事件绑定 ===
  onInputUsername(e) { this.setData({ username: e.detail.value }) },
  onInputPhone(e) { this.setData({ phone: e.detail.value }) },
  onInputPassword(e) { this.setData({ password: e.detail.value }) },
  onInputConfirm(e) { this.setData({ confirmPassword: e.detail.value }) },

  // === 2. 返回登录页 ===
  goBackLogin() {
    wx.navigateBack();
  },

  // === 3. 提交注册 ===
  handleRegister() {
    const { username, phone, password, confirmPassword } = this.data;

    // --- 前端校验开始 ---
    if (!username || !password || !phone) {
      return wx.showToast({ title: '请填写完整信息', icon: 'none' });
    }
    if (phone.length !== 11) {
      return wx.showToast({ title: '手机号格式不对', icon: 'none' });
    }
    if (password !== confirmPassword) {
      return wx.showToast({ title: '两次密码不一致', icon: 'none' });
    }
    // --- 前端校验结束 ---

    wx.showLoading({ title: '正在注册...' });

    // --- 发送请求 ---
    wx.request({
      url: 'http://localhost:8080/user/register', // 刚才写的后端接口
      method: 'POST',
      data: {
        username: username,
        password: password, // 后端会进行 MD5 加密
        phone: phone,
        sex: '1' // 默认性别，或者你可以加个单选框让用户选
      },
      success: (res) => {
        wx.hideLoading();
        console.log("注册返回：", res.data);

        // 判断后端返回状态 code=1 为成功
        if (res.data.code === 1) {
          wx.showToast({
            title: '注册成功',
            icon: 'success',
            duration: 2000
          });

          // 延迟 1.5秒 后返回登录页，让用户去登录
          setTimeout(() => {
            wx.navigateBack(); 
          }, 1500);

        } else {
          // 注册失败（比如账号已存在）
          wx.showToast({
            title: res.data.msg || '注册失败',
            icon: 'none'
          });
        }
      },
      fail: (err) => {
        wx.hideLoading();
        console.error(err);
        wx.showToast({ title: '服务器连接失败', icon: 'none' });
      }
    });
  }
})