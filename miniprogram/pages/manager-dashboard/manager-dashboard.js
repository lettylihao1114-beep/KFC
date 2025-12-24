const app = getApp();

Page({
  data: {
    // === 全局控制 ===
    currentMainTab: 0, // 0: 订单管理, 1: 菜品管理

    // === 订单模块数据 ===
    activeTab: 2, // 默认显示待接单
    orders: [],
    stats: {
      pending: 0,
      processing: 0,
      completed: 0
    },
    isRefreshing: false,

    // === 菜品模块数据 ===
    products: [],
    categories: []
  },

  onLoad() {
    // 页面加载时默认加载订单
    this.refreshAll();
  },

  onShow() {
    // 每次显示页面，根据当前大 Tab 刷新对应数据
    if (this.data.currentMainTab === 0) {
      this.refreshAll(); // 刷新订单
    } else {
      this.initProducts(); // 刷新菜品
    }
  },

  // ✨✨✨ 跳转回用户端 (去点餐) ✨✨✨
  goToUserSide() {
    console.log('正在返回点餐页...');
    // 使用 reLaunch 强制重启到首页，最稳妥
    wx.reLaunch({
      url: '/pages/index/index', 
      fail: () => {
        // 如果首页路径不对，尝试跳转菜单页
        wx.reLaunch({ url: '/pages/menu/menu' });
      }
    });
  },

  // 切换一级导航 (订单 <-> 菜品)
  switchMainTab(e) {
    const index = parseInt(e.currentTarget.dataset.index);
    this.setData({
      currentMainTab: index
    });

    if (index === 0) {
      this.refreshAll();
    } else {
      // 如果切到菜品且没有数据，或者是为了保持最新，都加载一次
      this.initProducts();
    }
  },

  // =================================================================
  //  模块一：订单管理逻辑
  // =================================================================

  onRefresh() {
    if (this.data.currentMainTab === 0) {
      this.setData({
        isRefreshing: true
      });
      this.refreshAll().then(() => {
        this.setData({
          isRefreshing: false
        });
      });
    }
  },

  refreshAll() {
    return Promise.all([
      this.fetchStats(),
      this.fetchOrders(this.data.activeTab)
    ]);
  },

  // 切换订单状态 Tab (待接单/制作中/已完成)
  switchTab(e) {
    const status = parseInt(e.currentTarget.dataset.status);
    this.setData({
      activeTab: status
    });
    this.fetchOrders(status);
  },

  fetchStats() {
    const p1 = this.requestList(2);
    const p2 = this.requestList(3);
    const p3 = this.requestList(4);

    return Promise.all([p1, p2, p3]).then(results => {
      this.setData({
        stats: {
          pending: results[0].length,
          processing: results[1].length,
          completed: results[2].length
        }
      });
    });
  },

  requestList(status) {
    return new Promise((resolve) => {
      // 🚨🚨🚨 关键修改：使用 admin_token 🚨🚨🚨
      const token = wx.getStorageSync('admin_token') || '';
      
      wx.request({
        url: `${app.globalData.baseUrl}/order/admin/list?status=${status}`,
        header: {
          'token': token // 给后端看管理员工牌
        },
        dataType: 'text', // 防止精度丢失
        success: (res) => {
          if (res.statusCode === 401 || (typeof res.data === 'string' && res.data.includes('No Permission'))) {
            // 如果没权限，resolve 空数组
            resolve([]);
            return;
          }
          try {
            let rawData = res.data;
            // 处理长数字ID
            rawData = rawData.replace(/"id":(\d{16,})/g, '"id":"$1"');
            const list = JSON.parse(rawData);
            resolve(list || []);
          } catch (e) {
            console.error('解析订单数据失败', e);
            resolve([]);
          }
        },
        fail: () => resolve([])
      });
    });
  },

  fetchOrders(status) {
    wx.showLoading({
      title: '加载订单...'
    });
    return this.requestList(status).then(list => {
      wx.hideLoading();
      list.forEach(item => {
        if (item.orderTime) {
          item.orderTimeFormatted = item.orderTime.replace('T', ' ').substring(5, 16);
        }
      });
      this.setData({
        orders: list
      });
    });
  },

  updateStatus(e) {
    const {
      id,
      status
    } = e.currentTarget.dataset;
    const that = this;
    let content = status === 3 ? '确认接单吗？' : '确认通知取餐吗？';

    wx.showModal({
      title: '提示',
      content: content,
      success(res) {
        if (res.confirm) {
          wx.showLoading({
            title: '处理中...'
          });
          
          // 🚨🚨🚨 关键修改：使用 admin_token 🚨🚨🚨
          const token = wx.getStorageSync('admin_token') || '';
          
          wx.request({
            url: `${app.globalData.baseUrl}/order/admin/status?orderId=${id}&status=${status}`,
            method: 'PUT',
            header: {
              'token': token // 给后端看管理员工牌
            },
            success(res) {
              wx.hideLoading();
              if (res.data === '操作成功' || res.statusCode === 200) {
                wx.showToast({
                  title: '操作成功',
                  icon: 'success'
                });
                that.refreshAll();
              } else {
                wx.showToast({
                  title: '操作失败',
                  icon: 'none'
                });
              }
            },
            fail() {
              wx.hideLoading();
              wx.showToast({
                title: '网络错误',
                icon: 'none'
              });
            }
          });
        }
      }
    });
  },

  viewDetail(e) {
    const id = e.currentTarget.dataset.id;
    wx.showModal({
      title: '详情',
      content: `订单ID: ${id}`,
      showCancel: false
    });
  },

  // =================================================================
  //  模块二：菜品管理逻辑
  // =================================================================

  initProducts() {
    const that = this;
    wx.showLoading({
      title: '加载菜品...'
    });

    // 1. 获取分类
    const p1 = new Promise((resolve) => {
      wx.request({
        url: `${app.globalData.baseUrl}/category/list`,
        success: res => {
          if (res.data && res.data.code === 1) {
            resolve(res.data.data || []);
          } else {
            resolve(res.data || []);
          }
        },
        fail: () => resolve([])
      });
    });

    // 2. 获取菜品
    const p2 = new Promise((resolve) => {
      wx.request({
        url: `${app.globalData.baseUrl}/product/list`,
        success: res => {
          if (res.data && res.data.code === 1) {
            resolve(res.data.data || []);
          } else {
            resolve(res.data || []);
          }
        },
        fail: () => resolve([])
      });
    });

    Promise.all([p1, p2]).then(([cats, prods]) => {
      wx.hideLoading();

      if (!Array.isArray(prods)) {
        console.warn('菜品数据不是数组，可能拆包失败:', prods);
        prods = [];
      }

      const processedList = prods.map(item => {
        const cid = item.categoryId || item.category_id;
        const cat = cats.find(c => String(c.id) === String(cid));
        return {
          ...item,
          categoryName: cat ? cat.name : '未分类',
          status: Number(item.status)
        };
      });

      that.setData({
        categories: cats,
        products: processedList
      });
    });
  },

  // 删除菜品
  handleDelete(e) {
    const id = e.currentTarget.dataset.id;
    const that = this;
    
    // 🚨 获取 admin_token
    const token = wx.getStorageSync('admin_token');

    wx.showModal({
      title: '警告',
      content: '确定要删除此商品吗？',
      confirmColor: '#d62f35',
      success(res) {
        if (res.confirm) {
          wx.request({
            url: `${app.globalData.baseUrl}/product?ids=${id}`,
            method: 'DELETE',
            // ✨✨✨ 补全 Header，防止 401 ✨✨✨
            header: {
                'token': token
            },
            success(apiRes) {
              const isSuccess = apiRes.statusCode === 200 && (apiRes.data.code === 1 || apiRes.data === '删除成功');
              if (isSuccess) {
                wx.showToast({
                  title: '已删除'
                });
                that.initProducts();
              } else {
                wx.showToast({
                  title: '删除失败',
                  icon: 'none'
                });
              }
            }
          });
        }
      }
    });
  },

  // 菜品上下架
  toggleStatus(e) {
    const id = e.currentTarget.dataset.id;
    const newStatus = e.detail.value ? 1 : 0;
    
    // 🚨 获取 admin_token
    const token = wx.getStorageSync('admin_token');

    wx.request({
      url: `${app.globalData.baseUrl}/product/status/${newStatus}?ids=${id}`,
      method: 'POST',
      // ✨✨✨ 补全 Header，防止 401 ✨✨✨
      header: {
          'token': token
      },
      success(res) {
        const isSuccess = res.statusCode === 200 && (res.data.code === 1 || res.data === '状态已更新');
        if (!isSuccess) {
          wx.showToast({
            title: '操作失败',
            icon: 'none'
          });
          this.initProducts();
        }
      },
      fail: () => {
        wx.showToast({
          title: '网络异常',
          icon: 'none'
        });
        this.initProducts();
      }
    });
  },

  // 跳转去新建
  goToAdd() {
    wx.navigateTo({
      url: '/pages/product-edit/product-edit'
    });
  },

  // 跳转去编辑 (已包含数据回显逻辑)
  goToEdit(e) {
    // 1. 拿到 wxml 里传过来的完整对象 (data-product)
    const product = e.currentTarget.dataset.product;
    
    console.log('准备编辑:', product);

    // 2. 转成字符串并编码 (防止中文乱码)
    const productStr = encodeURIComponent(JSON.stringify(product));
    
    // 3. 跳转，带上 id 和 product 字符串
    wx.navigateTo({
      url: `/pages/product-edit/product-edit?id=${product.id}&product=${productStr}`
    });
  }
})