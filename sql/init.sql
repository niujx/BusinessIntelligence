-- 作为测试用的表
 CREATE TABLE `User` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1


-- 系统表
饿了吗评论表
CREATE TABLE `eleme_evaluate` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `shopId` bigint(50) DEFAULT NULL,
  `date` varchar(30) DEFAULT NULL,
  `evaValue` varchar(200) DEFAULT NULL,
  `star` int(20) DEFAULT NULL,
  `goods` varchar(80) DEFAULT NULL,
  `num` int(10) DEFAULT NULL,
  `type` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;