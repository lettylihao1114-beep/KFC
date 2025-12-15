-- 1. 创建数据库
CREATE DATABASE IF NOT EXISTS kfc_ordering DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;
USE kfc_ordering;

-- 2. 创建商品表 (测试用)
CREATE TABLE `product` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `name` varchar(64) NOT NULL COMMENT '菜品名称',
                           `price` decimal(10,2) NOT NULL COMMENT '价格',
                           `image` varchar(255) DEFAULT NULL COMMENT '图片',
                           `status` tinyint(1) DEFAULT '1' COMMENT '1上架 0下架',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜品表';

-- 3. 插入两条数据 (给等会儿的测试留点东西)
INSERT INTO `product` (`name`, `price`, `status`) VALUES
                                                      ('香辣鸡腿堡', 19.50, 1),
                                                      ('冰可乐', 7.00, 1);