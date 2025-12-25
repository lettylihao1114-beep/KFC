const app = getApp()

Page({
  data: {
    addressList: [],
    showForm: false,
    form: {
      consignee: '',
      phone: '',
      detail: '',
      label: '家'
    }
  },

  onLoad(options) {
      // 1. 检查是否是选择模式
      if (options.selectMode) {
          this.setData({ selectMode: true });
          wx.setNavigationBarTitle({ title: '选择收货地址' });
      }
  },

  onShow() {
    this.loadAddressList()
  },

  // ...

  // ✨✨✨ 新增：选择地址逻辑 ✨✨✨
  selectAddress(e) {
      // 只有在 selectMode 模式下才生效
      if (!this.data.selectMode) return;

      const item = e.currentTarget.dataset.item;
      console.log('选择了地址:', item);

      // 1. 存入全局变量
      app.globalData.selectedAddress = item;
      
      // 2. 跳转去菜单页
      wx.switchTab({
          url: '/pages/menu/menu'
      });
  },

  loadAddressList() {
    const user = app.globalData.user
    if (!user) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    const that = this
    wx.request({
      url: `http://localhost:8080/addressBook/list?userId=${user.id}`,
      method: 'GET',
      success(res) {
        if (res.statusCode === 200 && res.data) {
          that.setData({ addressList: res.data })
        }
      }
    })
  },

  openForm() {
    this.setData({
      showForm: true,
      form: {
        consignee: '',
        phone: '',
        detail: '',
        label: '家'
      }
    })
  },

  closeForm() {
    this.setData({ showForm: false })
  },

  onInputChange(e) {
    const field = e.currentTarget.dataset.field
    const value = e.detail.value
    const form = this.data.form
    form[field] = value
    this.setData({ form })
  },

  saveAddress() {
    const user = app.globalData.user
    if (!user) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    const form = this.data.form
    if (!form.consignee || !form.phone || !form.detail) {
      wx.showToast({ title: '请完整填写信息', icon: 'none' })
      return
    }
    const that = this
    wx.request({
      url: 'http://localhost:8080/addressBook/add',
      method: 'POST',
      data: {
        userId: user.id,
        consignee: form.consignee,
        phone: form.phone,
        detail: form.detail,
        label: form.label,
        isDefault: 0
      },
      success(res) {
        if (res.statusCode === 200) {
          wx.showToast({ title: '保存成功', icon: 'success' })
          that.setData({ showForm: false })
          that.loadAddressList()
        } else {
          wx.showToast({ title: '保存失败', icon: 'none' })
        }
      },
      fail() {
        wx.showToast({ title: '网络错误', icon: 'none' })
      }
    })
  },

  onSetDefault(e) {
    const user = app.globalData.user
    if (!user) {
      wx.showToast({ title: '请先登录', icon: 'none' })
      return
    }
    const item = e.currentTarget.dataset.item
    const payload = Object.assign({}, item, { userId: user.id, isDefault: 1 })
    const that = this
    wx.request({
      url: 'http://localhost:8080/addressBook/default',
      method: 'PUT',
      data: payload,
      success(res) {
        if (res.statusCode === 200) {
          wx.showToast({ title: '设置成功', icon: 'success' })
          that.loadAddressList()
        } else {
          wx.showToast({ title: '设置失败', icon: 'none' })
        }
      },
      fail() {
        wx.showToast({ title: '网络错误', icon: 'none' })
      }
    })
  },

  onLongPressAddress(e) {
      const item = e.currentTarget.dataset.item;
      const that = this;
      wx.showModal({
          title: '删除地址',
          content: '确定要删除这个地址吗？',
          success(res) {
              if (res.confirm) {
                  wx.request({
                      url: `http://localhost:8080/addressBook?id=${item.id}`,
                      method: 'DELETE',
                      success(res2) {
                          if (res2.statusCode === 200) {
                              wx.showToast({ title: '删除成功' });
                              that.loadAddressList();
                          } else {
                              wx.showToast({ title: '删除失败', icon: 'none' });
                          }
                      }
                  });
              }
          }
      });
  }
})

