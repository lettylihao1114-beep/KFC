const app = getApp();

Page({
  data: {
    cartList: [],
    totalPrice: 0,
    orderType: 'dinein', // 'dinein' | 'takeout'
    shop: {},
    address: null,
    user: null,
    dineOption: 'in' // 'in' 堂食 | 'out' 外带 (仅自取有效)
  },

  onLoad(options) {
    // 从全局获取预存的数据
    const preview = app.globalData.orderPreview;
    if (preview) {
      this.setData({
        cartList: preview.cartList,
        totalPrice: preview.totalPrice,
        orderType: preview.orderType,
        shop: preview.shop,
        address: preview.address,
        user: preview.user
      });
    } else {
      wx.showToast({ title: '数据加载异常', icon: 'none' });
      setTimeout(() => wx.navigateBack(), 1500);
    }
  },

  onShow() {
      // 检查是否有重新选的地址
      if (this.data.orderType === 'takeout' && app.globalData.selectedAddress) {
          this.setData({ address: app.globalData.selectedAddress });
      }
  },

  setDineOption(e) {
      this.setData({ dineOption: e.currentTarget.dataset.opt });
  },

  reselectAddress() {
      wx.navigateTo({
          url: '/pages/address/address?selectMode=true'
      });
  },

  submitOrder() {
    const that = this;
    const { user, totalPrice, shop, orderType, address, dineOption } = this.data;

    if (!user) return;

    // 再次校验
    if (orderType === 'takeout' && !address) {
        wx.showToast({ title: '请选择收货地址', icon: 'none' });
        return;
    }

    wx.showLoading({ title: '正在提交...' });

    const orderData = {
      userId: user.id,
      amount: parseFloat(totalPrice),
      shopId: shop ? shop.id : 1,
      // 传递额外信息：
      remark: orderType === 'takeout' ? '外送' : (dineOption === 'in' ? '堂食' : '外带'),
      addressBookId: orderType === 'takeout' ? address.id : null,
      payMethod: 1 // 微信支付
    };

    wx.request({
      url: `${app.globalData.baseUrl}/order/create`,
      method: 'POST',
      data: orderData,
      success(res) {
        wx.hideLoading();
        // 兼容 R 对象
        if (res.statusCode === 200) {
            let responseData = res.data;
            if (responseData && responseData.code === 1) {
              responseData = responseData.data;
            }

            const responseMsg = typeof responseData === 'string' ? responseData : JSON.stringify(responseData);
            // 提取订单ID
            const match = responseMsg.match(/(\d+)/);
            if (match) {
                that.payOrder(match[0]);
            } else {
                // 某些后端直接返回成功消息
                wx.showModal({
                    title: '下单成功',
                    content: '订单已提交',
                    showCancel: false,
                    success() {
                        that.clearCartAndGoHome();
                    }
                });
            }
        } else {
            wx.showToast({ title: '下单失败', icon: 'none' });
        }
      },
      fail() {
        wx.hideLoading();
        wx.showToast({ title: '网络错误', icon: 'none' });
      }
    });
  },

  payOrder(orderId) {
    const that = this;
    wx.showLoading({ title: '正在支付...' });
    wx.request({
      url: `${app.globalData.baseUrl}/order/pay?orderId=${orderId}`,
      method: 'POST',
      success() {
        wx.hideLoading();
        wx.showModal({
          title: '支付成功',
          content: '您的餐点正在制作中，请耐心等待！',
          showCancel: false,
          success() {
            that.clearCartAndGoHome();
          }
        });
      },
      fail() {
        wx.hideLoading();
        wx.showToast({ title: '支付请求失败', icon: 'none' });
      }
    });
  },

  clearCartAndGoHome() {
    const user = app.globalData.user;
    // 清空购物车
    wx.request({
        url: `${app.globalData.baseUrl}/shoppingCart/clean?userId=${user.id}`,
        method: 'DELETE',
        complete: () => {
            // 清除全局预览数据
            app.globalData.orderPreview = null;
            // 跳转回首页
            wx.switchTab({ url: '/pages/index/index' });
        }
    });
  }
});