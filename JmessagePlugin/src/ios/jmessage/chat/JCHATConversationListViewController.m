// *************************************************
// *************************************************
// 会话列表控制器
// 会话列表数据请求、本地存储、添加好友等
// *************************************************
// *************************************************

#import "JCHATConversationListViewController.h"
//#import "JCHATConversationListCell.h"
#import "JCHATConversationViewController.h"
//#import "JCHATSelectFriendsCtl.h"
#import "MBProgressHUD+Add.h"
//#import "JCHATAlertViewWait.h"
//#import "JCHATAlreadyLoginViewController.h"
#import "AppDelegate.h"
#import "BaseJMessage.h"
#import "JmessagePlugin.h"
#import "JCHATStringUtils.h"
#import "LogUtils.h"

//#define kBackBtnFrame CGRectMake(0, 0, 50, 30)
//#define kBubbleBtnColor UIColorFromRGB(0x4880d7)

@interface JCHATConversationListViewController ()
{
  __block NSMutableArray *_conversationArr;
    // 消息通知回调
    NotifyBlock jmessageNotifyBlock;
    // 会话列表回调
    ConverListBlock converListCallbackBlock;
    // 会话消息列表回调
    ConversationViewBlock conversationViewBlack;


//  UIButton *_rightBarButton;
  NSInteger _unreadCount;
//  UILabel *_titleLabel;
}
@end

@implementation JCHATConversationListViewController

// 单例模式：只被GCD初始化一次
+(JCHATConversationListViewController*) getInstance{
    static JCHATConversationListViewController *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}


// 初始化
- (void)initCtrl{

//  [super viewDidLoad];

    NSLog(@"--------------- initConverListCtrl:initCtrl --------------");

  DDLogDebug(@"Action - viewDidLoad");

//  [self setupNavigation];

  [self addNotifications];

//  [self.view setBackgroundColor:[UIColor whiteColor]];
//  [self setupBubbleView];
//  [self setupChatTable];

//  AppDelegate *appDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;

    BaseJMessage *baseJMessage = [BaseJMessage getInstance];
    if (!baseJMessage.isDBMigrating) {
      [self addDelegate];
    } else {
        // 正在升级数据库
      NSLog(@"JMessage -------- 正在升级数据库 -------");
      //[MBProgressHUD showMessage:@"正在升级数据库" toView:self.view];
    }

}

//- (void)setupNavigation {
//  self.navigationController.navigationBar.translucent = NO;
//  self.navigationController.interactivePopGestureRecognizer.delegate = self;
//  self.title = @"会话";
//  _rightBarButton = [UIButton buttonWithType:UIButtonTypeCustom];
//  [_rightBarButton setFrame:kBackBtnFrame];
//  [_rightBarButton addTarget:self action:@selector(addBtnClick:) forControlEvents:UIControlEventTouchUpInside];
//  [_rightBarButton setImage:[UIImage imageNamed:@"createConversation"] forState:UIControlStateNormal];
//  [_rightBarButton setImageEdgeInsets:UIEdgeInsetsMake(0, 0, 0, -15*[UIScreen mainScreen].scale)];
//  self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:_rightBarButton];//为导航栏添加右侧按钮
//}



// 通知更新数据
- (void)addNotifications {

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(netWorkConnectClose)
                                               name:kJPFNetworkDidCloseNotification
                                             object:nil];

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(netWorkConnectSetup)
                                               name:kJPFNetworkDidSetupNotification
                                             object:nil];

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(connectSucceed)
                                               name:kJPFNetworkDidLoginNotification
                                             object:nil];

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(isConnecting)
                                               name:kJPFNetworkIsConnectingNotification
                                             object:nil];

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(dBMigrateFinish)
                                               name:kDBMigrateFinishNotification object:nil];

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(alreadyLoginClick)
                                               name:kLogin_NotifiCation object:nil];

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(creatGroupSuccessToPushView:)
                                               name:kCreatGroupState
                                             object:nil];

  [[NSNotificationCenter defaultCenter] addObserver:self
                                           selector:@selector(skipToSingleChatView:)
                                               name:kSkipToSingleChatViewState
                                             object:nil];
}




//- (void)setupBubbleView {
//  _addBgView = [[UIImageView alloc] initWithFrame:CGRectMake(kApplicationWidth - 100, 1, 100, 100)];
//  [_addBgView setBackgroundColor:[UIColor clearColor]];
//  [_addBgView setUserInteractionEnabled:YES];
//  UIImage *frameImg = [UIImage imageNamed:@"frame"];
//  frameImg = [frameImg resizableImageWithCapInsets:UIEdgeInsetsMake(30, 10, 30, 64) resizingMode:UIImageResizingModeTile];
//  [_addBgView setImage:frameImg];
//  [_addBgView setHidden:YES];
//  [self.view addSubview:self.addBgView];
//  [self.view bringSubviewToFront:self.addBgView];
//  [self addBtn];
//}



//- (void)setupChatTable {
//  [_chatTableView setBackgroundColor:[UIColor whiteColor]];
//  _chatTableView.dataSource=self;
//  _chatTableView.delegate=self;
//  _chatTableView.separatorStyle=UITableViewCellSeparatorStyleNone;
//  _chatTableView.touchDelegate = self;
//
//  [_chatTableView registerNib:[UINib nibWithNibName:@"JCHATConversationListCell" bundle:nil] forCellReuseIdentifier:@"JCHATConversationListCell"];
//}


- (void)addDelegate {
  [JMessage addDelegate:self withConversation:nil];
}


// 跳转到单例会话页面
- (void)skipToSingleChatView :(NSNotification *)notification {

  JMSGUser *user = [[notification object] copy];

//  __block JCHATConversationViewController *sendMessageCtl =[JCHATConversationViewController getInstance];//!!
//
////  __weak typeof(self)weakSelf = self;
////  __weak __typeof(&*self)weakSelf = self;
//
//  sendMessageCtl.superViewController = self;

  [JMSGConversation createSingleConversationWithUsername:user.username appKey:JMESSAGE_APPKEY completionHandler:^(id resultObject, NSError *error) {

//    __strong __typeof(weakSelf)strongSelf = weakSelf;

    if (error == nil) {
        JMSGConversation * conversation = resultObject;

//      sendMessageCtl.conversation = conversation;

      // 主线程操作
      JCHATMAINTHREAD(^{
//        sendMessageCtl.hidesBottomBarWhenPushed = YES;
//        [strongSelf.navigationController pushViewController:sendMessageCtl animated:YES];

          if(conversationViewBlack){
              conversationViewBlack(conversation);
          }

      });
    } else {
      DDLogDebug(@"createSingleConversationWithUsername");
    }
  }];
}

- (void)dBMigrateFinish {
  NSLog(@"Migrate is finish  and get allconversation");
  JCHATMAINTHREAD(^{
      // 数据库升级结束
     //[MBProgressHUD hideAllHUDsForView:self.view animated:YES];
  });

  [self addDelegate];
  [self getConversationList];
}

// 根据用户名获取会话
- (JMSGConversation *)getConversationWithTargetId:(NSString *)targetId {
  for (NSInteger i=0; i< [_conversationArr count]; i++) {
    JMSGConversation *conversation = [_conversationArr objectAtIndex:i];

    if (conversation.conversationType == kJMSGConversationTypeSingle) {
      if ([((JMSGUser *)conversation.target).username isEqualToString:targetId]) {
        return conversation;
      }
    } else {
      if ([((JMSGGroup *)conversation.target).gid isEqualToString:targetId]) {
        return conversation;
      }
    }
  }
  DDLogDebug(@"Action getConversationWithTargetId  fail to meet conversation");
  return nil;
}

// 跳转到会话详情消息列表控制器
- (void)skipToConversationController:(NSString *)targetId converType:(NSString*)type callback:(ConversationViewBlock)callback {

    for (NSInteger i=0; i< [_conversationArr count]; i++) {

        JMSGConversation *conversation = [_conversationArr objectAtIndex:i];

        if (conversation.conversationType == kJMSGConversationTypeSingle) {

            if ([((JMSGUser *)conversation.target).username isEqualToString:targetId]) {
                if(callback){
                    callback(conversation);
                }
                return;
            }

        } else {

            if ([((JMSGGroup *)conversation.target).gid isEqualToString:targetId]) {
                if(callback){
                    callback(conversation);
                }
                return;
            }

        }
    }
    DDLogDebug(@"Action getConversationWithTargetId  fail to meet conversation");
    return;
}


// 重新加载会话信息
- (void)reloadConversationInfo:(JMSGConversation *)conversation {
  DDLogDebug(@"Action - creatGroupSuccessToPushView - %@", conversation);
  for (NSInteger i=0; i<[_conversationArr count]; i++) {
    JMSGConversation *conversationObject = [_conversationArr objectAtIndex:i];
    if ([conversationObject.target isEqualToConversation:conversation.target]) {

      [_conversationArr removeObjectAtIndex:i];
      [_conversationArr insertObject:conversation atIndex:i];

        // 加载信息
//      [_chatTableView reloadData];

      return;
    }
  }
}

#pragma mark --创建群成功Push group viewctl
- (void)creatGroupSuccessToPushView:(NSNotification *)object{//group
  DDLogDebug(@"Action - creatGroupSuccessToPushView - %@", object);

//  __block JCHATConversationViewController *sendMessageCtl =[JCHATConversationViewController getInstance];
//
////  __weak __typeof(self)weakSelf = self;
//
//  sendMessageCtl.superViewController = self;
////  sendMessageCtl.hidesBottomBarWhenPushed=YES;

    // 根据群组ID创建群组会话
  [JMSGConversation createGroupConversationWithGroupId:((JMSGGroup *)[object object]).gid completionHandler:^(id resultObject, NSError *error) {

//    __strong __typeof(weakSelf)strongSelf = weakSelf;

    if (error == nil) {

        JMSGConversation * conversation = resultObject;

//      sendMessageCtl.conversation = (JMSGConversation *)resultObject;
        // 主线程中操作
      JCHATMAINTHREAD(^{
          // 切换到指定页面
//        [strongSelf.navigationController pushViewController:sendMessageCtl animated:YES];

          if(conversationViewBlack){
              conversationViewBlack(conversation);
          }
      });

    } else {
      DDLogDebug(@"createGroupConversationwithgroupid fail");
    }
  }];

}

- (void)viewWillAppear:(BOOL)animated {
//  [super viewWillAppear:YES];
//
//  AppDelegate *appDelegate = (AppDelegate *) [UIApplication sharedApplication].delegate;

    NSLog(@"///////////  conversation list view viewWillAppear ///////////");

    BaseJMessage *baseJMessage = [BaseJMessage getInstance];

  if (!baseJMessage.isDBMigrating) {
    [self getConversationList];
  } else {
      // 正在升级数据库
    NSLog(@"is DBMigrating don't get allconversations");
    //[MBProgressHUD showMessage:@"正在升级数据库" toView:self.view];
  }

//  if ([self.navigationController respondsToSelector:@selector(interactivePopGestureRecognizer)]) {
//  }
}

- (void)netWorkConnectClose {
  DDLogDebug(@"Action - netWorkConnectClose");
  //_titleLabel.text =@"未连接";
}

- (void)netWorkConnectSetup {
  DDLogDebug(@"Action - netWorkConnectSetup");
  //_titleLabel.text =@"收取中...";
}

- (void)connectSucceed {
  DDLogDebug(@"Action - connectSucceed");
  //_titleLabel.text =@"会话";
}

- (void)isConnecting {
  DDLogDebug(@"Action - isConnecting");
  //_titleLabel.text =@"连接中...";
}


#pragma mark JMSGMessageDelegate
- (void)onReceiveMessage:(JMSGMessage *)message
                   error:(NSError *)error {

  DDLogDebug(@"Action -- onReceivemessage %@",message);
  DDLogDebug(@"JMSGMessage -- messageToJson %@", [JMessageJSONUtils formatJMSGMessageToJson:message]);


    // 通知接口回调
    if(jmessageNotifyBlock){

        dispatch_async(dispatch_get_global_queue(0, 0), ^{

            // 处理耗时操作的代码块...
            NSString *resultData = [JMessageJSONUtils formatJMSGMessageToJson:message];

            //通知主线程刷新
            dispatch_async(dispatch_get_main_queue(), ^{
                //回调或者说是通知主线程刷新
                jmessageNotifyBlock(NotifyConverJMSGMessage, resultData);
            });

        });
    }

  [self getConversationList];
}



- (void)onConversationChanged:(JMSGConversation *)conversation {
  DDLogDebug(@"Action -- onConversationChanged");
  [self getConversationList];
}

- (void)onGroupInfoChanged:(JMSGGroup *)group {
  DDLogDebug(@"Action -- onGroupInfoChanged");
  [self getConversationList];
}

- (void)viewDidAppear:(BOOL)animated {
  DDLogDebug(@"Action - viewDidAppear");
  //[super viewDidAppear:YES];

}

- (void)viewDidDisappear:(BOOL)animated {
  DDLogDebug(@"Action - viewDidDisappear");
  //[super viewDidDisappear:YES];
}

// 获取会话列表
- (void)getConversationList:(ConverListBlock)callback{
    converListCallbackBlock = callback;
    [self getConversationList];
}

// 获取会话列表
- (void)getConversationList {
  //[self.addBgView setHidden:YES];

  [JMSGConversation allConversations:^(id resultObject, NSError *error) {
    JCHATMAINTHREAD(^{

      if (error == nil) {
        _conversationArr = [self sortConversation:resultObject];
        _unreadCount = 0;
        for (NSInteger i=0; i < [_conversationArr count]; i++) {

          JMSGConversation *conversation = [_conversationArr objectAtIndex:i];
          _unreadCount = _unreadCount + [conversation.unreadCount integerValue];

        }

        [self saveBadge:_unreadCount];

      } else {
        _conversationArr = nil;
      }

      //[self.chatTableView reloadData];

        if(converListCallbackBlock){

            dispatch_async(dispatch_get_global_queue(0, 0), ^{

                // 处理耗时操作的代码块...
                NSString *converListJson = [JMessageJSONUtils conversationListToJson:_conversationArr];

                [LogUtils debug:[NSString stringWithFormat:@")))))))))))))))))))))))))))) 会话列表： %@", converListJson]];

                //通知主线程刷新
                dispatch_async(dispatch_get_main_queue(), ^{
                    //回调或者说是通知主线程刷新
                    converListCallbackBlock(&(_unreadCount), converListJson);
                });

            });
        }

    });
  }];
}

// 分类／分组排序
NSInteger sortType(id object1,id object2,void *cha) {
  JMSGConversation *model1 = (JMSGConversation *)object1;
  JMSGConversation *model2 = (JMSGConversation *)object2;
  if([model1.latestMessage.timestamp integerValue] > [model2.latestMessage.timestamp integerValue]) {
    return NSOrderedAscending;
  } else if([model1.latestMessage.timestamp integerValue] < [model2.latestMessage.timestamp integerValue]) {
    return NSOrderedDescending;
  }
  return NSOrderedSame;
}

#pragma mark --排序conversation
- (NSMutableArray *)sortConversation:(NSMutableArray *)conversationArr {
  NSArray *sortResultArr = [conversationArr sortedArrayUsingFunction:sortType context:nil];
  return [NSMutableArray arrayWithArray:sortResultArr];
}


// 已登陆
- (void)alreadyLoginClick {
  [self getConversationList];
}


//- (void)addBtn {
//  for (NSInteger i=0; i<2; i++) {
//    UIButton *btn =[UIButton buttonWithType:UIButtonTypeCustom];
//    if (i==0) {
//      [btn setTitle:@"发起群聊" forState:UIControlStateNormal];
//    }
//    if (i==1) {
//      [btn setTitle:@"添加朋友" forState:UIControlStateNormal];
//    }
//    [btn addTarget:self action:@selector(btnClick:) forControlEvents:UIControlEventTouchUpInside];
//    btn.tag=i + 100;
//    [btn setFrame:CGRectMake(10, i*30+30, 80, 30)];
//    [btn.titleLabel setFont:[UIFont systemFontOfSize:16]];
//    [btn setBackgroundImage:[ViewUtil colorImage:kBubbleBtnColor frame:btn.frame] forState:UIControlStateHighlighted];
//    [self.addBgView addSubview:btn];
//  }
//}


//- (void)btnClick :(UIButton *)btn {
//  [self.addBgView setHidden:YES];
//  if (btn.tag == 100) {
//    JCHATSelectFriendsCtl *selectCtl =[[JCHATSelectFriendsCtl alloc] init];
//    UINavigationController *selectNav =[[UINavigationController alloc] initWithRootViewController:selectCtl];
//    [self.navigationController presentViewController:selectNav animated:YES completion:nil];
//  } else if (btn.tag == 101) {
//    UIAlertView *alerView =[[UIAlertView alloc] initWithTitle:@"添加好友"
//                                                      message:@"输入好友用户名!"
//                                                     delegate:self
//                                            cancelButtonTitle:@"取消"
//                                            otherButtonTitles:@"确定", nil];
//    alerView.alertViewStyle =UIAlertViewStylePlainTextInput;
//    [alerView show];
//  }
//}



//- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
//  if (buttonIndex == 0) {
//  } else if (buttonIndex == 1)
//  {
//    if ([[alertView textFieldAtIndex:0].text isEqualToString:@""]) {
//      [MBProgressHUD showMessage:@"请输入用户名" view:self.view];
//      return;
//    }
//
//    [[JCHATAlertViewWait ins] showInView];
//
//    __block JCHATConversationViewController *sendMessageCtl = [JCHATConversationViewController getInstance];
//
//    sendMessageCtl.superViewController = self;
//    sendMessageCtl.hidesBottomBarWhenPushed = YES;
//
//    [[alertView textFieldAtIndex:0] resignFirstResponder];
//    __weak __typeof(self)weakSelf = self;
//
//      // 根据用户名创建会话（添加好友）
//    [JMSGConversation createSingleConversationWithUsername:[alertView textFieldAtIndex:0].text appKey:JMESSAGE_APPKEY completionHandler:^(id resultObject, NSError *error) {
//
//      [[JCHATAlertViewWait ins] hidenAll];
//
//      if (error == nil) {
//        [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
//        __strong __typeof(weakSelf) strongSelf = weakSelf;
//
//        sendMessageCtl.conversation = resultObject;
//
//        [strongSelf.navigationController pushViewController:sendMessageCtl animated:YES];
//      } else {
//        DDLogDebug(@"createSingleConversationWithUsername fail");
//        [MBProgressHUD showMessage:@"添加的用户不存在" view:self.view];
//      }
//    }];
//  }
//}

// 添加好友
-(void) addFriend:(NSString*) username callback:(RequestResultBlock)callback
{
    __block JCHATConversationViewController *sendMessageCtl = [JCHATConversationViewController getInstance];
    sendMessageCtl.superViewController = self;
//    sendMessageCtl.hidesBottomBarWhenPushed = YES;

//    __weak __typeof(self)weakSelf = self;

    // 根据用户名创建会话（添加好友）
    [JMSGConversation createSingleConversationWithUsername:username appKey:JMESSAGE_APPKEY completionHandler:^(id resultObject, NSError *error) {

        if (error == nil) {

//            __strong __typeof(&*weakSelf)strongSelf = weakSelf;

            sendMessageCtl.conversation = resultObject;

            // 成功后跳转到会话页面（单体／群组）
            if(callback){
                callback(kJMSGRequestStatusSuc, resultObject);
            }

//            [strongSelf.navigationController pushViewController:sendMessageCtl animated:YES];

        } else {
            // 添加的用户不存在
            if(callback){
                callback(kJMSGRequestStatusFail, error);
            }
            DDLogDebug(@"createSingleConversationWithUsername fail");
        }
    }];
}

//- (void)addBtnClick:(UIButton *)btn {
//  if (btn.selected) {
//    btn.selected=NO;
//    [self.addBgView setHidden:YES];
//  } else {
//    btn.selected=YES;
//    [self.addBgView setHidden:NO];
//  }
//  [self.view bringSubviewToFront:self.addBgView];
//}

//- (void)perFormAdd {
//
//}

//- (void)tableView:(UITableView *)tableView
//     touchesBegan:(NSSet *)touches
//        withEvent:(UIEvent *)event {
//  [self.addBgView setHidden:YES];
//  _rightBarButton.selected=NO;
//}
//
////修改编辑按钮文字
//- (NSString *)tableView:(UITableView *)tableView titleForDeleteConfirmationButtonForRowAtIndexPath:(NSIndexPath *)indexPath {
//  return @"删除";
//}
//
////先设置Cell可移动
//- (BOOL)tableView:(UITableView *)tableView canMoveRowAtIndexPath:(NSIndexPath *)indexPath {
//  return YES;
//}

// 进入编辑模式，按下出现的编辑按钮后
//- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
//
//  DDLogDebug(@"Action - tableView");
//
//    // 点击会话列表跳转进入会话页面
//  JMSGConversation *conversation = [_conversationArr objectAtIndex:indexPath.row];
//
//  if (conversation.conversationType == kJMSGConversationTypeSingle) {
//
//    [JMSGConversation deleteSingleConversationWithUsername:((JMSGUser *)conversation.target).username appKey:JMESSAGE_APPKEY
//     ];
//
//  } else {
//
//    [JMSGConversation deleteGroupConversationWithGroupId:((JMSGGroup *)conversation.target).gid];
//
//  }
//
//  [_conversationArr removeObjectAtIndex:indexPath.row];
//
//
//
//  [tableView deleteRowsAtIndexPaths:@[indexPath] withRowAnimation:UITableViewRowAnimationNone];
//
//}

//- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
//  return 1;
//}


//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//  if ([_conversationArr count] > 0) {
//    return [_conversationArr count];
//  } else{
//    return 0;
//  }
//}


// 绑定数据
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//  static NSString *cellIdentifier = @"JCHATConversationListCell";
//
//  JCHATConversationListCell *cell = (JCHATConversationListCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
//
//  JMSGConversation *conversation =[_conversationArr objectAtIndex:indexPath.row];
//  [cell setCellDataWithConversation:conversation];
//  return cell;
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
//  return 65;
//}


#pragma mark - SearchBar Delegate
//- (void)searchBarTextDidBeginEditing:(UISearchBar *)_searchBar {
//
//}

// 响应点击索引时的委托方法
//- (NSInteger)tableView:(UITableView *)tableView sectionForSectionIndexTitle:(NSString *)title atIndex:(NSInteger)index {
//  return [_conversationArr count];
//}

//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
//  UITableViewCell * cell = [tableView cellForRowAtIndexPath:indexPath];
//  cell.selected = NO;
//
//
//  JCHATConversationViewController *sendMessageCtl =[JCHATConversationViewController getInstance];
//
//  sendMessageCtl.hidesBottomBarWhenPushed = YES;
//  sendMessageCtl.superViewController = self;
//
//
//  JMSGConversation *conversation = [_conversationArr objectAtIndex:indexPath.row];
//  sendMessageCtl.conversation = conversation;
//
//  [self.navigationController pushViewController:sendMessageCtl animated:YES];
//
//  NSInteger badge = _unreadCount - [conversation.unreadCount integerValue];
//  [self saveBadge:badge];
//}


// 记录未读消息条数
- (void)saveBadge:(NSInteger)badge {
  [[NSUserDefaults standardUserDefaults] setObject:[NSString stringWithFormat:@"%zd",badge] forKey:kBADGE];
  [[NSUserDefaults standardUserDefaults] synchronize];
}

// Via Jack Lucky
//- (void)searchBarCancelButtonClicked:(UISearchBar *)_searchBar {
//
//}
//
//- (void)didReceiveMemoryWarning {
//  [super didReceiveMemoryWarning];
//}

/*
 #pragma mark - Navigation

 // In a storyboard-based application, you will often want to do a little preparation before navigation
 - (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
 // Get the new view controller using [segue destinationViewController].
 // Pass the selected object to the new view controller.
 }
 */
//- (void)updateSearchResultsForSearchController:(UISearchController *)searchController {
//
//}


/**
 *  通知信息回调
 */
- (void)setNotifyMessageCallback:(NotifyBlock)notifyBlock{
    jmessageNotifyBlock = notifyBlock;
}


/**
 * 加载会话头像
 */
- (void)loadConverListAvatar:targetId callback:(NotifyBlock)callback{

    dispatch_async(dispatch_get_global_queue(0, 0), ^{

        JMSGConversation *conversation = [self getConversationWithTargetId:targetId];
        NSString *conversationId = [self conversationIdWithConversation:conversation];
        [conversation avatarData:^(NSData *data, NSString *objectId, NSError *error) {

            if (![objectId isEqualToString:conversationId]) {
                NSLog(@"out-of-order avatar");
                //通知主线程刷新
                dispatch_async(dispatch_get_main_queue(), ^{
                    callback(NotifyErrorCallback, @"out-of-order avatar");
                });
                return ;
            }

            if (error == nil) {
                if (data != nil) {
                    //[self.headView setImage:[UIImage imageWithData:data]];
                    //通知主线程刷新
                    dispatch_async(dispatch_get_main_queue(), ^{

                        // 返回头像地址

                        callback(LoadAvatarCallback, @"");
                    });
                } else {
                    //通知主线程刷新
                    dispatch_async(dispatch_get_main_queue(), ^{
                        callback(LoadAvatarCallback, @"");
                    });
                }
            } else {
                //通知主线程刷新
                dispatch_async(dispatch_get_main_queue(), ^{
                    callback(NotifyErrorCallback, [JCHATStringUtils errorAlert:error]);
                });
            }
        }];

    });
}


- (NSString *)conversationIdWithConversation:(JMSGConversation *)conversation {
    NSString *conversationId = nil;
    if (conversation.conversationType == kJMSGConversationTypeSingle) {
        JMSGUser *user = conversation.target;
        conversationId = [NSString stringWithFormat:@"%@_%ld_%@",user.username, kJMSGConversationTypeSingle, conversation.targetAppKey];
    } else {
        JMSGGroup *group = conversation.target;
        conversationId = [NSString stringWithFormat:@"%@_%ld",group.gid,kJMSGConversationTypeGroup];
    }
    return conversationId;
}

/**
 * 获取完整的头像地址
 *
 */
- (NSString*)getFullConverListAvatarPath:(NSString*)avatarPath{
    // 测试
    NSArray *paths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory,NSUserDomainMask,YES);
    NSString *filePath = paths[0];

    JMSGUser *user = [JMSGUser myInfo];
    NSString *userPath = [NSString stringWithFormat:@"/%@_%@/", user.username, user.appKey];

    NSString *fullAvatarPath = [NSString stringWithFormat:@"%@%@%@", filePath, userPath, avatarPath];
    [LogUtils debug:[NSString stringWithFormat:@"&& ------------------> fullAvatarPath: %@", fullAvatarPath]];
    return fullAvatarPath;
}

@end
