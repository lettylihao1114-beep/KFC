-- ==========================================
-- KFC 点餐系统数据库初始化脚本
-- 版本: v1.0
-- 描述: 包含建库、建表及测试数据
-- ==========================================

-- 1. 创建数据库 (如果不存在的话)
CREATE DATABASE IF NOT EXISTS kfc_ordering 
DEFAULT CHARSET utf8mb4 
COLLATE utf8mb4_general_ci;

-- 2. 切换到该数据库
USE kfc_ordering;

-- ==========================================
-- 表结构设计
-- ==========================================

-- 3. 创建商品表 (Product)
-- 对应 Java 实体类: com.kfc.backend.entity.Product
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(64) NOT NULL COMMENT '菜品名称',
  `price` decimal(10,2) NOT NULL COMMENT '当前价格',
  `image` varchar(255) DEFAULT NULL COMMENT '图片URL',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态: 1上架, 0下架',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品信息表';

-- ==========================================
-- 初始化测试数据 (Mock Data)
-- ==========================================

-- 4. 插入测试数据
INSERT INTO `product` (`id`, `name`, `price`, `image`, `status`) VALUES 
(1, '香辣鸡腿堡', 19.50, 'https://img.kfc.com.cn/burger1.jpg', 1),
(2, '劲脆鸡腿堡', 19.50, 'https://img.kfc.com.cn/burger2.jpg', 1),
(3, '冰镇可乐(中)', 7.00, 'https://img.kfc.com.cn/cola.jpg', 1),
(4, '吮指原味鸡', 12.00, NULL, 1),
(5, '已下架的薯条', 11.00, NULL, 0);

-- 打印成功信息
SELECT 'Database initialized successfully!' as status;