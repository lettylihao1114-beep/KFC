// 数据库配置
// 注意：此文件已被 .gitignore 忽略，不会提交到 Git
// 队友 clone 后需要新建此文件并填入自己的密码
module.exports = {
  host: 'localhost',
  user: 'root',
  password: '123456', // 你的本地密码
  database: 'ordering_db',
  waitForConnections: true,
  connectionLimit: 10,
  queueLimit: 0
};
