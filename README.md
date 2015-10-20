like
=============================
介绍
------- 
    用于记录用户喜欢了什么事物
	整个系统的设计原则是，将其设计为一个与业务系统无关的独立系统
	设计量级100万用户，2000万记录
	任何业务系统都可以将其作为一个子模块进行部署
系统角色
------- 
    User 用户
    OpsAdmin 系统管理员
整个系统的业务实体为
------- 
    用户：user
    事物：target

用户用例
------- 
####User
    我Like了哪些物体？
    我Like了某个物品
    我取消了某物品的Like
    我修改了我的信息
    我是否喜欢了某个Target
####OpsAdmin相关
    Target 被哪些用户Like了
    Target 被Like的数目
    Target Like 排行榜
    Target 信息发生了修改
设计
------- 

####假设
	假设每个用户平均Like20个target，一个用户最大Like100个
	再假设整个系统用户100万，即最多有2000万条数据
	每条数据1K，共20G数据量
####喜欢记录(LikeRecord)
	一般情况下想到的是RDS和MongoDB
	但是RDS是默认事务性的，它的效率在相同条件下远差于MongoDB 
	同时Like记录本身是一个独立的实体，他不需要关联其他实体操作，所以用MongoDB也不会有问题
	Like系统本身业务觉得了其可以不太精确（丢失小部分数据也没太大关系）
	MongoDB的开发成本比RDS低
	所以采用MongoDB记录用户喜欢事物的记录UserLikeRecord(id userinfo，targetinfo，date，status)
####用户喜欢记录(UserLikeRecord)**
	用MongoDB完全可以在UserLikeRecord表中搜索出用户的喜欢列表，但是这会对整个MongoDB造成巨大压力，它需要遍历整个数据库才能找到用户喜欢了哪些Target
	虽然可以自建索引，但是效率仍然不高<br> 
	同时用户打开喜欢界面是比较频繁的操作，这对性能提出了更高的要求<br> 
	如果将用户喜欢的Target数据采用单独的数据结构记录起来会减少这种压力<br> 
	在此我采用Redis记录用户喜欢的Target集合，Redis为键值型数据库，内置了一些基本数据结构比如List<br> 
	UserLike表{UserLike_userid:List<UserLikeRecord>}记录用户喜欢的Target列表<br> 
	这样用户查找喜欢的列表的时候不需要通过Mongo search,只需要调用redis键值查询既可以获得用户所有的喜欢物品集合，而且速度极快
	
####物品被喜欢记录(TargetLikedRecord)**
	有时Ops需要给喜欢了某物品的用户发消息或者邮件，这种需求频次极低；
	同Like记录的设计原则，我们也采用Redis作为存储数据库，有一点区别是value只需要存储Id即可，同时不需要顺序	
	TargetLiked表 {TargetLike_tagrtid:Set<userid>}记录target被喜欢的用户列表
	
####物品喜欢排行榜(TargetLikedRank)**
	Redis内部数据结构ZSet非常适合排行榜
	TargetLikedRank表 {TargetLikedRank_targetId,value}记录target被喜欢的次数
实现
------- 

####插入一条用户 Like记录需实现如下步骤
	判断用户是否已经Like过Target,yes返回错误
	这时候即可返回Success了，因为Like系统不需要太精确，所以如下步骤可以异步执行
		插入一条LikeRecord
		往UserLikeRecord中插入LikeRecord
		往TargetLikedRecord中插入userid
		往TargetLikedRank进行+1操作
		
####取消一个Like需要实现如下步骤
	判断用户是否已经Like过Target，no返回错误
	这时候即可返回Success了，因为Like系统不需要太精确，所以如下步骤可以异步执行
		移除一条LikeRecord
		遍历User的UserLikeRecord移除LikeRecord
		从TargetLikedRecord中移除Target对应的UserId
		//TargetLikedRank 一般情况不进行-1操作

部署
-----



