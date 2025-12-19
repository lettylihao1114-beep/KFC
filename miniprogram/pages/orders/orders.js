const app = getApp();

Page({
  data: {
    orders: []
  },

  onShow() {
    this.checkLoginAndFetch();
  },

  checkLoginAndFetch() {
    const user = app.globalData.user;
    if (user) {
      this.fetchOrders(user.id);
    } else {
      wx.showToast({ title: '请先登录', icon: 'none' });
      setTimeout(() => {
        wx.navigateBack();
      }, 1500);
    }
  },

  fetchOrders(userId) {
    const that = this;
    wx.showLoading({ title: '加载中...' });
    wx.request({
      url: `http://localhost:8080/order/user/list?userId=${userId}`,
      success(res) {
        wx.hideLoading();
        if(res.data) {
          // 格式化时间
          const list = res.data.map(item => {
            if(item.orderTime) item.orderTime = item.orderTime.replace('T', ' ');
            return item;
          });
          that.setData({ orders: list });
        }
      },
      fail() {
        wx.hideLoading();
        wx.showToast({ title: '网络错误', icon: 'none' });
      }
    });
  },

  payOrder(e) {
    const orderId = e.currentTarget.dataset.id;
    const that = this;
    wx.showModal({
      title: '支付确认',
      content: '确定要支付该订单吗？',
      success(res) {
        if (res.confirm) {
          wx.showLoading({ title: '支付中...' });
          wx.request({
            url: `http://localhost:8080/order/pay?orderId=${orderId}`,
            method: 'POST',
            success(payRes) {
              wx.hideLoading();
              if (payRes.statusCode === 200 && (payRes.data === '支付成功' || payRes.data.includes('成功'))) {
                wx.showToast({ title: '支付成功', icon: 'success' });
                // 刷新列表
                const user = app.globalData.user;
                if (user) {
                   that.fetchOrders(user.id);
                }
              } else {
                wx.showToast({ title: '支付失败', icon: 'none' });
              }
            },
            fail() {
              wx.hideLoading();
              wx.showToast({ title: '网络错误', icon: 'none' });
            }
          });
        }
      }
    });
  }
})
