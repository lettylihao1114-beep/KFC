const app = getApp();

Page({
  data: {
    id: null,
    name: '',
    category: '',
    price: '',
    description: '',
    image: '', 
    categories: ['主食', '小食', '甜品/饮料', '套餐'] 
  },

  onLoad(options) {
    // 1. 如果有 id，说明是编辑模式
    if (options.id) {
      // 2. 尝试获取传过来的完整数据
      if (options.product) {
        try {
          const product = JSON.parse(decodeURIComponent(options.product));
          
          this.setData({
            id: product.id,
            name: product.name,
            category: product.category || product.categoryName, // 兼容字段
            price: product.price,
            description: product.description,
            // 如果图片是 http 开头的网络图才显示，本地路径或者无效路径不显示
            image: (product.image && product.image.startsWith('http')) ? product.image : '' 
          });
          
          wx.setNavigationBarTitle({ title: '编辑菜品' });
        } catch (e) {
          console.error('解析菜品数据失败', e);
        }
      }
    } else {
      wx.setNavigationBarTitle({ title: '新增菜品' });
    }
  },

  // 输入事件绑定
  onInputName(e) { this.setData({ name: e.detail.value }) },
  onInputPrice(e) { this.setData({ price: e.detail.value }) },
  onInputDesc(e) { this.setData({ description: e.detail.value }) },
  
  // 分类选择
  onCategoryChange(e) {
    this.setData({
      category: this.data.categories[e.detail.value]
    })
  },

  // 选择图片
  chooseImage() {
    const that = this;
    wx.chooseImage({
      count: 1, 
      sizeType: ['compressed'], 
      sourceType: ['album', 'camera'], 
      success(res) {
        const tempPath = res.tempFilePaths[0];
        console.log('📸 已选择图片:', tempPath);
        that.setData({ image: tempPath });
      }
    })
  },

  // ✨✨✨ 核心修复：提交保存 ✨✨✨
  submitForm() {
    const { id, name, category, price, description, image } = this.data;

    if (!name || !price || !category) {
      wx.showToast({ title: '请填写完整信息', icon: 'none' });
      return;
    }

    const formData = {
      id: id,
      name: name,
      category: category,
      price: parseFloat(price),
      description: description,
      image: image 
    };

    // 🚨🚨🚨 关键修改：取 admin_token (管理员令牌)，而不是普通 token 🚨🚨🚨
    const token = wx.getStorageSync('admin_token');
    
    // 如果没有管理员 Token，说明登录过期，踢回登录页
    if (!token) {
        wx.showToast({ title: '登录过期，请重登', icon: 'none' });
        setTimeout(() => {
            // 使用 redirectTo 避免层级叠加
            wx.redirectTo({ url: '/pages/admin-login/admin-login' });
        }, 1500);
        return;
    }

    const baseUrl = app.globalData.baseUrl;
    // 有 id 调更新接口，没 id 调新增接口
    const url = id ? `${baseUrl}/product/update` : `${baseUrl}/product/add`;

    console.log(`🚀 正在提交到: ${url}`);
    console.log('📦 携带管理员Token:', token);

    wx.request({
      url: url,
      method: 'POST', 
      data: formData,
      // ✨✨✨ 把真正的管理员 Token 给后端 ✨✨✨
      header: { 
          'content-type': 'application/json',
          'token': token 
      }, 
      success(res) {
        // 如果后端铁面无私还是报 401
        if (res.statusCode === 401) {
             wx.showToast({ title: '权限不足，请重新登录', icon: 'none' });
             setTimeout(() => {
                wx.redirectTo({ url: '/pages/admin-login/admin-login' });
             }, 1500);
             return;
        }

        if (res.statusCode === 200) {
          wx.showToast({ title: '保存成功', icon: 'success' });
          setTimeout(() => {
            wx.navigateBack(); // 保存成功后自动返回上一页
          }, 1500);
        } else {
          console.error('保存失败:', res);
          wx.showToast({ title: '保存失败', icon: 'error' });
        }
      },
      fail(err) {
        console.error(err);
        wx.showToast({ title: '网络错误', icon: 'none' });
      }
    });
  }
});