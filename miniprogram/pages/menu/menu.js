const app = getApp()

Page({
  data: {
    // 模拟数据
    categories: [
      { id: 0, name: '人气热卖', icon: '🔥' },
      { id: 1, name: '帕尼尼' },
      { id: 2, name: '现熬好粥' },
      { id: 3, name: '大饼' },
      { id: 4, name: '吐司' },
      { id: 5, name: '发面小笼' },
      { id: 6, name: '多人餐' },
      { id: 7, name: '金奖豆' }
    ],
    menuData: [
      {
        id: 0,
        category: '人气热卖',
        items: [
          { id: 101, name: 'K记发面小笼包', sub: '豆浆二件套', price: 14.0, originalPrice: 20.0, image: '', tag: '大神卡¥8.4起' },
          { id: 102, name: '大饼卷黑椒牛肉蛋', sub: '豆浆二件套', price: 19.0, originalPrice: 25.0, image: '', tag: '大神卡¥11.4起' },
          { id: 103, name: '6元随心配', sub: '6元随心配', price: 6.0, originalPrice: 13.0, image: '', tag: '' }
        ]
      },
      {
        id: 1,
        category: '帕尼尼',
        items: [
          { id: 201, name: '芝士猪柳帕尼尼', sub: '两件套', price: 9.6, originalPrice: 26.0, image: '', tag: '' }
        ]
      }
      // 更多数据...
    ],
    activeCategory: 0,
    toView: 'category-0',
    cartCount: 0,
    totalPrice: 0
  },

  onLoad() {
  },

  onShow() {
    // 隐藏系统导航栏，因为 Image 2 显示了自定义头部（搜索框等）
    // 实际开发通常使用 navigationStyle: custom
  },

  switchCategory(e) {
    const index = e.currentTarget.dataset.index;
    this.setData({
      activeCategory: index,
      toView: `category-${index}`
    });
  },

  goBack() {
    wx.navigateBack();
  },

  // 选规格
  showSpec(e) {
    const item = e.currentTarget.dataset.item;
    wx.showToast({
      title: `选择规格: ${item.name}`,
      icon: 'none'
    });
    // 这里应该弹窗选择规格
  }
});