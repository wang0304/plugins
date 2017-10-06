// *************************************************
// *************************************************
// 群组信息详情控制器
// 显示群组的信息详情
// *************************************************
// *************************************************

#import "JCHATGroupDetailViewController.h"
//#import "JCHATGroupMemberCollectionViewCell.h"
//#import "JCHATGroupDetailFooterReusableView.h"
//#import "JCHATPersonViewController.h"
#import "JCHATFriendDetailViewController.h"
//#import "JCHATFootTableCollectionReusableView.h"
//#import "JCHATFootTableViewCell.h"
#import "AppDelegate.h"
#import "BaseJMessage.h"
#import <JMessage/JMSGGroup.h>
#import <JMessage/JMSGUser.h>

#import "JCHATStringUtils.h"
#import "LogUtils.h"


@interface JCHATGroupDetailViewController ()

//<UICollectionViewDataSource,
//UICollectionViewDelegate,
//UICollectionViewDelegateFlowLayout,
//UIAlertViewDelegate,
//UITableViewDataSource,
//UITabBarDelegate>

{
  BOOL _isInEditToDeleteMember;
  BOOL _isNoDisturb;
}

//@property (weak, nonatomic) IBOutlet UICollectionView *groupMemberGrip;

@end

@implementation JCHATGroupDetailViewController


// 单例模式：只被GCD初始化一次
+(JCHATGroupDetailViewController*) getInstance{
    static JCHATGroupDetailViewController *instance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}


- (void)initCtrl:(CDVInvokedUrlCommand*)command {
//  [super viewDidLoad];

  _isInEditToDeleteMember = NO;

//  [self setupNavigationBar];


//  if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//
//    _memberArr = @[_conversation.target];
//
//    [self setupGroupMemberGrip];
//    _isNoDisturb = ((JMSGUser *)_conversation.target).isNoDisturb;
//
//  }else {
//
//    [self getAllMember];
//    [self setupGroupMemberGrip];
//    _isNoDisturb = ((JMSGGroup *)_conversation.target).isNoDisturb;
//
//  }

}

/**
 * 刷新用户列表
 */
- (void)refreshMemberGrid:(RequestResultBlock)callback {
    [self getAllMember: callback];

//  [_groupMemberGrip reloadData];
}

//- (void)setupNavigationBar {
//  self.title=@"聊天详情";
//  UIButton *leftBtn =[UIButton buttonWithType:UIButtonTypeCustom];
//  [leftBtn setFrame:kNavigationLeftButtonRect];
//  [leftBtn setImage:[UIImage imageNamed:@"goBack"] forState:UIControlStateNormal];
//  [leftBtn setImageEdgeInsets:kGoBackBtnImageOffset];
//  [leftBtn addTarget:self
//              action:@selector(backClick)
//    forControlEvents:UIControlEventTouchUpInside];
//  self.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:leftBtn];//为导航栏添加左侧按钮
//  self.navigationController.navigationBar.translucent = NO;
//}
//
//- (void)setupGroupMemberGrip {
//  _groupMemberGrip.minimumZoomScale = 0;
//  [_groupMemberGrip registerClass:[UICollectionViewCell class] forCellWithReuseIdentifier:@"gradientCell"];
//  [_groupMemberGrip registerNib:[UINib nibWithNibName:@"JCHATGroupMemberCollectionViewCell" bundle:nil]
//     forCellWithReuseIdentifier:@"JCHATGroupMemberCollectionViewCell"];
//
//  [_groupMemberGrip registerNib:[UINib nibWithNibName:@"JCHATFootTableCollectionReusableView" bundle:nil]
//     forSupplementaryViewOfKind:UICollectionElementKindSectionFooter
//            withReuseIdentifier:@"JCHATFootTableCollectionReusableView"];
//  _groupMemberGrip.backgroundColor = [UIColor whiteColor];
//  _groupMemberGrip.delegate = self;
//  _groupMemberGrip.dataSource = self;
//  _groupMemberGrip.backgroundColor = [UIColor clearColor];
//
//  _groupMemberGrip.backgroundView = [UIView new];
//  UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapGripMember:)];
//  [_groupMemberGrip.backgroundView addGestureRecognizer:tap];
//}

- (void)tapGripMember:(id)sender {
  [self removeEditStatus];
}

- (void)removeEditStatus {
  _isInEditToDeleteMember = NO;
//  [_groupMemberGrip reloadData];
}

//- (void)backClick {
//  [self.navigationController popViewControllerAnimated:YES];
//}



// 获取所有群组成员信息
-(void)getAllMember:(RequestResultBlock)callback{
    _memberArr = [((JMSGGroup *)(_conversation.target)) memberArray];
    if(callback) callback(0, _memberArr);
}



#pragma mark - CollectionViewDelegate
//- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
//  if (section != 0) {
//    return 0;
//  }
//
//  if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//    return _memberArr.count + 1;
//  }
//
//  JMSGGroup *group = (JMSGGroup *)_conversation.target;
//  if ([group.owner isEqualToString:[JMSGUser myInfo].username]) {
//    return _memberArr.count + 2;
//  } else {
//    return _memberArr.count +1;
//  }
//}
//
//- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
//  return 1;
//}
//
//- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(nonnull NSIndexPath *)indexPath {
//  return CGSizeMake(52, 80);
//}
//
//- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForFooterInSection:(NSInteger)section {
//  return CGSizeMake(kApplicationWidth, 270);
//}
//
//- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
//  static NSString *CellIdentifier = @"JCHATGroupMemberCollectionViewCell";
//  JCHATGroupMemberCollectionViewCell *cell = (JCHATGroupMemberCollectionViewCell *)[collectionView dequeueReusableCellWithReuseIdentifier:CellIdentifier forIndexPath:indexPath];
//
//  if (indexPath.item == _memberArr.count) {
//    [cell setAddMember];
//    return cell;
//  }
//
//  if (indexPath.item == _memberArr.count + 1) {
//    [cell setDeleteMember];
//    return cell;
//  }
//
//  if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//    JMSGUser *user = _memberArr[indexPath.item];
//    [cell setDataWithUser:user withEditStatus:_isInEditToDeleteMember];
//  } else {
//    JMSGGroup *group = _conversation.target;
//    JMSGUser *user = _memberArr[indexPath.item];
//    if ([group.owner isEqualToString:user.username]) {
//      [cell setDataWithUser:user withEditStatus:NO];
//    } else {
//      [cell setDataWithUser:user withEditStatus:_isInEditToDeleteMember];
//    }
//
//  }
//
//  return cell;
//}


// 获取用户信息
- (void)getGroupInfo:(RequestResultBlock)callback{
    if(!_conversation){
        if(callback) callback(-1, @"群组信息为空！");
        return;
    }

    JMSGGroup *group = _conversation.target;
    if(group){
        if(callback) callback(0, group);
    } else {
        if(callback) callback(-1, @"群组信息为空！");
    }

}


// 获取群组成员对象
-(void)getUserByIndex:(unsigned int)index callback:(RequestResultBlock)callback{
    JMSGUser *user = nil;
    if (_conversation.conversationType == kJMSGConversationTypeSingle) {

        user = _memberArr[index];
        //[cell setDataWithUser:user withEditStatus:_isInEditToDeleteMember];

    } else {

        JMSGGroup *group = _conversation.target;
        user = _memberArr[index];
        // 群组对象如果是自己，则可编辑，否则不可编辑
        if ([group.owner isEqualToString:user.username]) {
            //[cell setDataWithUser:user withEditStatus:NO];

        } else {

            //[cell setDataWithUser:user withEditStatus:_isInEditToDeleteMember];
        }
    }

    if(callback) callback(0, user);
}


// 清空会话所有消息（BaiMei add）
-(void)deleteAllMessages{
    if(self.conversation) [self.conversation deleteAllMessages];
    [[NSNotificationCenter defaultCenter] postNotificationName:kDeleteAllMessage object:nil];
}


// 添加群成员
-(void)addMemberWithUsername:(NSString*)username callback:(RequestResultBlock)callback{
    if (_conversation.conversationType == kJMSGConversationTypeSingle) {
        [self addMemberToGroup:username callback:callback];
    } else {

        //__weak __typeof(self)weakSelf = self;
        //[MBProgressHUD showMessage:@"获取成员信息" toView:self.view];

        [((JMSGGroup *)(self.conversation.target)) addMembersWithUsernameArray:@[username]
             completionHandler:^(id resultObject, NSError *error) {

                //[MBProgressHUD hideAllHUDsForView:weakSelf.view animated:YES];
                if (error == nil) {
                    // 添加成员成功

                    //__strong __typeof(weakSelf)strongSelf = weakSelf;
                    //[strongSelf refreshMemberGrid];
                    NSString *errorStr = [JCHATStringUtils errorAlert:error];
                    if(callback) callback(0, @"添加成员成功！");

                } else {
                    // 添加成员失败
                    DDLogDebug(@"addMembersFromUsernameArray fail with error %@",error);

                    //[MBProgressHUD showMessage:@"添加成员失败" view:weakSelf.view];
                    NSString *errorStr = [JCHATStringUtils errorAlert:error];
                    if(callback) callback(-1, errorStr);
                }
        }];
    }
}


// 添加群成员
-(void)addMemberWithUsernameArray:(NSArray*)userNameArray callback:(RequestResultBlock)callback{
    if (_conversation.conversationType == kJMSGConversationTypeSingle) {
        [self addMemberToGroup:userNameArray[0] callback:callback];
    } else {

        //__weak __typeof(self)weakSelf = self;
        //[MBProgressHUD showMessage:@"获取成员信息" toView:self.view];

        [((JMSGGroup *)(self.conversation.target)) addMembersWithUsernameArray:userNameArray
                completionHandler:^(id resultObject, NSError *error) {

                //[MBProgressHUD hideAllHUDsForView:weakSelf.view animated:YES];
                    if (error == nil) {
                        // 添加成员成功

                        //__strong __typeof(weakSelf)strongSelf = weakSelf;
                        //[strongSelf refreshMemberGrid];

                        if(callback) callback(0, @"添加成员成功！");

                    } else {
                        // 添加成员失败
                        DDLogDebug(@"addMembersFromUsernameArray fail with error %@",error);
                        //[MBProgressHUD showMessage:@"添加成员失败" view:weakSelf.view];
                        NSString *errorStr = [JCHATStringUtils errorAlert:error];
                        if(callback) callback(-1, errorStr);
                    }
        }];
    }
}

/**
 * 添加好友到单聊
 * @param
 */
-(void)addMemberToGroup:(NSString*)username callback:(RequestResultBlock)callback{
    [self createGroup:username callback:callback];
}

/**
 * 创建群组
 * @param
 */
-(void)createGroup:(NSString*)username callback:(RequestResultBlock)callback{

    __block JMSGGroup *tmpgroup =nil;
    __typeof__(self) __weak weakSelf = self;

    NSArray *memberArray = nil;
    if(username){
        memberArray = @[((JMSGUser *)self.conversation.target).username, username];
    }

    [JMSGGroup createGroupWithName:@"" desc:@"" memberArray:memberArray completionHandler:^(id resultObject, NSError *error) {

        //[MBProgressHUD hideAllHUDsForView:self.view animated:YES];

        __typeof__(weakSelf) __strong strongSelf = weakSelf;

        tmpgroup = (JMSGGroup *)resultObject;

        if (error == nil) {

            [JMSGConversation createGroupConversationWithGroupId:tmpgroup.gid completionHandler:^(id resultObject, NSError *error) {
                // 创建群成功
                if (error == nil) {
                    // [MBProgressHUD showMessage:@"创建群成功" view:self.view];
                    JMSGConversation *groupConversation = (JMSGConversation *)resultObject;

                    strongSelf.sendMessageCtl.conversation = groupConversation;
                    strongSelf.sendMessageCtl.isConversationChange = YES;

                    [JMessage removeDelegate:strongSelf.sendMessageCtl withConversation:_conversation];
                    [JMessage addDelegate:strongSelf.sendMessageCtl withConversation:groupConversation];

                    // 跳转到会话页面
//                    strongSelf.sendMessageCtl.targetName = tmpgroup.name;
//                    strongSelf.sendMessageCtl.title = tmpgroup.name;
//                    [strongSelf.sendMessageCtl setupView];

                    if(callback) callback(0, groupConversation);

                    // 发送通知
                    [[NSNotificationCenter defaultCenter] postNotificationName:kConversationChange object:resultObject];

                    // 更新导航
                    //[strongSelf.navigationController popViewControllerAnimated:YES];

                } else {
                    // 创建群失败
                    DDLogDebug(@"creategroupconversation error with error : %@",error);
                    NSString *errorStr = [JCHATStringUtils errorAlert:error];
                    if(callback) callback(-1, errorStr);
                }
            }];

        } else {
            //[MBProgressHUD showMessage:[JCHATStringUtils errorAlert:error] view:self.view];
            DDLogDebug(@"createGroupConversationWithGroupId error with error : %@",error);
            NSString *errorStr = [JCHATStringUtils errorAlert:error];
            if(callback) callback(-1, errorStr);
        }
    }];
}

// 退出群组
-(void)quitGroup:(RequestResultBlock)callback{
    //__weak __typeof(self)weakSelf = self;
    // [MBProgressHUD showMessage:@"正在推出群组！" toView:self.view];

    JMSGGroup *deletedGroup = ((JMSGGroup *)(self.conversation.target));

    [deletedGroup exit:^(id resultObject, NSError *error) {

        //[MBProgressHUD hideAllHUDsForView:weakSelf.view animated:YES];

        if (error == nil) {
            // 退出群组成功
            DDLogDebug(@"推出群组成功");
            if(callback) callback(0, @"退出群组成功！");

//            //[MBProgressHUD showMessage:@"推出群组成功" view:weakSelf.view];
//            [JMSGConversation deleteGroupConversationWithGroupId:deletedGroup.gid];
//            //[self.navigationController popToViewController:self.sendMessageCtl.superViewController animated:YES];

        } else {
            // 退出群组失败
            DDLogDebug(@"推出群组失败");
            //[MBProgressHUD showMessage:@"推出群组失败" view:weakSelf.view];
            NSString *errorStr = [JCHATStringUtils errorAlert:error];
            if(callback) callback(-1, errorStr);
        }
    }];
}

/**
 * 删除群组会话
 * @param groupId 群组ID
 */
- (void)deleteGroupConversation:(NSString*)groupId callback:(RequestResultBlock)callback{

    if([JMSGConversation deleteGroupConversationWithGroupId:groupId]){
        if(callback) callback(0, @"");
    } else {
        if(callback) callback(-1, @"");
    }

}

// 更新群组名称
-(void)updateGroupName:(NSString*)newGroupName callback:(RequestResultBlock)callback{
    JMSGGroup *needUpdateGroup = (JMSGGroup *)(self.conversation.target);
    [JMSGGroup updateGroupInfoWithGroupId:needUpdateGroup.gid
                                     name:newGroupName
                                     desc:needUpdateGroup.desc
                        completionHandler:^(id resultObject, NSError *error) {

                            //[MBProgressHUD hideAllHUDsForView:self.view animated:YES];

                            if (error == nil) {
                                // 更新群组名称成功
                                //[MBProgressHUD showMessage:@"更新群组名称成功" view:self.view];
                                //[self refreshMemberGrid];
                                if(callback) callback(0, @"更新群组名称成功!");
                            } else {
                                // 更新群组名称失败
                                //[MBProgressHUD showMessage:@"更新群组名称失败" view:self.view];
                                NSString *errorStr = [JCHATStringUtils errorAlert:error];
                                if(callback) callback(-1, errorStr);
                            }
                        }];
}

// 根据索引获取好友信息
-(void)getMemberByIndex:(unsigned int)index callback:(RequestResultBlock)callback{
    //  点击群成员头像
    JMSGUser *user = _memberArr[index];
//    JMSGGroup *group = _conversation.target;

        if ([user.username isEqualToString:[JMSGUser myInfo].username]) {
            // 跳转我的信息页面
//            JCHATPersonViewController *personCtl =[[JCHATPersonViewController alloc] init];
//            personCtl.hidesBottomBarWhenPushed = YES;
//            [self.navigationController pushViewController:personCtl animated:YES];
        } else {

            // 跳转好友信息页面
//            JCHATFriendDetailViewController *friendCtl = [[JCHATFriendDetailViewController alloc]initWithNibName:@"JCHATFriendDetailViewController" bundle:nil];
//            friendCtl.userInfo = user;
//            friendCtl.isGroupFlag = YES;
//            [self.navigationController pushViewController:friendCtl animated:YES];

        }
}


// 根据索引删除群组成员
-(void)deleteMemberByIndex:(unsigned int)index callback:(RequestResultBlock)callback{
    //  点击群成员头像
    JMSGUser *user = _memberArr[index];
    JMSGGroup *group = _conversation.target;

    if ([user.username isEqualToString:group.owner]) {
        return;
    }
    JMSGUser *userToDelete = _memberArr[index];
    [self deleteMemberWithUserName:userToDelete.username callback:callback];
}



// 根据用户名称删除群组成员
-(void)delMemberWithUsername:(NSString*)username callback:(RequestResultBlock)callback{
    //  点击群成员头像
    JMSGGroup *group = _conversation.target;
    if ([username isEqualToString:group.owner]) {
        return;
    }
    [self deleteMemberWithUserName:username callback:callback];
}






- (void)deleteMemberWithUserName:(NSString *)userName callback:(RequestResultBlock)callback{
    if ([_memberArr count] == 1) {
        return;
    }

    //[MBProgressHUD showMessage:@"正在删除好友！" toView:self.view];

    [((JMSGGroup *)(self.conversation.target)) removeMembersWithUsernameArray:@[userName]
            completionHandler:^(id resultObject, NSError *error) {

                if (error == nil) {
                    // 删除成员成功！
                    if(callback) callback(0, @"删除成员成功！");
                } else {
                    // 删除成员错误！
                    DDLogDebug(@"Group fail to removeMembersFromUsernameArrary");
                    NSString *errorStr =[JCHATStringUtils errorAlert:error];
                    if(callback) callback(-1, errorStr);
                }
    }];
}


/**
 * 根据用户名数组删除好友
 * @param userNameArray 用户账号数组
 */
- (void)deleteMemberWithUserNameArray:(NSArray *)userNameArray callback:(RequestResultBlock)callback{
    if ([_memberArr count] == 1) {
        return;
    }

    //[MBProgressHUD showMessage:@"正在删除好友！" toView:self.view];

    [((JMSGGroup *)(self.conversation.target)) removeMembersWithUsernameArray:userNameArray
            completionHandler:^(id resultObject, NSError *error) {

                if (error == nil) {
                    // 删除成员成功！
                    if(callback) callback(0, @"删除成员成功！");
                } else {
                    // 删除成员错误！
                    DDLogDebug(@"Group fail to removeMembersFromUsernameArrary");
                    NSString *errorStr =[JCHATStringUtils errorAlert:error];
                    if(callback) callback(-1, errorStr);
                }
    }];
}





//- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath {
//  JCHATFootTableCollectionReusableView *footTable = nil;
//  static NSString *footerIdentifier = @"JCHATFootTableCollectionReusableView";
//  footTable = [_groupMemberGrip dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter
//                                                                                    withReuseIdentifier:footerIdentifier
//                                                                                           forIndexPath:indexPath];
//  footTable.footTableView.delegate = self;
//  footTable.footTableView.dataSource =self;
//  [footTable.footTableView reloadData];
//  return footTable;
//}
//
//- (void)tapToEditGroupName {
//  UIAlertView *alerView = [[UIAlertView alloc] initWithTitle:@"输入群名称"
//                                                     message:@""
//                                                    delegate:self
//                                           cancelButtonTitle:@"取消"
//                                           otherButtonTitles:@"确定", nil];
//  alerView.alertViewStyle = UIAlertViewStylePlainTextInput;
//  alerView.tag = kAlertViewTagRenameGroup;
//  [alerView show];
//  [self removeEditStatus];
//}
//
//- (void)tapToClearChatRecord {
//  UIAlertView *alerView = [[UIAlertView alloc] initWithTitle:@"清空聊天记录"
//                                                     message:@""
//                                                    delegate:self
//                                           cancelButtonTitle:@"取消"
//                                           otherButtonTitles:@"确定", nil];
//  alerView.tag = kAlertViewTagClearChatRecord;
//  [alerView show];
//  [self removeEditStatus];
//}
//
//- (void)quitGroup:(UIButton *)btn
//{
//  UIAlertView *alerView = [[UIAlertView alloc] initWithTitle:@"退出群聊"
//                                                     message:@""
//                                                    delegate:self
//                                           cancelButtonTitle:@"取消"
//                                           otherButtonTitles:@"确定", nil];
//  alerView.tag = kAlertViewTagQuitGroup;
//  [alerView show];
//  [self removeEditStatus];
//}





//- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex
//{
//  if (buttonIndex == 0)return;
//
//  switch (alertView.tag) {
//    case kAlertViewTagClearChatRecord:
//    {
//      if (buttonIndex ==1) {
//        [self.conversation deleteAllMessages];
//        [[NSNotificationCenter defaultCenter] postNotificationName:kDeleteAllMessage object:nil];
//      }
//    }
//      break;
//    case kAlertViewTagAddMember:
//    {
//
//      if ([[alertView textFieldAtIndex:0].text isEqualToString:@""]) {
//        return;
//      }
//      if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//        [self createGroupWithAlertView:alertView];
//      } else {
//        __weak __typeof(self)weakSelf = self;
//        [MBProgressHUD showMessage:@"获取成员信息" toView:self.view];
//        [((JMSGGroup *)(self.conversation.target)) addMembersWithUsernameArray:@[[alertView textFieldAtIndex:0].text] appKey:JMESSAGE_APPKEY
//                                                             completionHandler:^(id resultObject, NSError *error) {
//                                                               [MBProgressHUD hideAllHUDsForView:weakSelf.view animated:YES];
//                                                               if (error == nil) {
//                                                                 __strong __typeof(weakSelf)strongSelf = weakSelf;
//                                                                 [strongSelf refreshMemberGrid];
//                                                               } else {
//                                                                 DDLogDebug(@"addMembersFromUsernameArray fail with error %@",error);
//                                                                 [MBProgressHUD showMessage:@"添加成员失败" view:weakSelf.view];
//                                                               }
//                                                             }];
//      }
//    }
//      break;
//    case kAlertViewTagQuitGroup:
//    {
//      if (buttonIndex ==1) {
//        __weak __typeof(self)weakSelf = self;
//        [MBProgressHUD showMessage:@"正在推出群组！" toView:self.view];
//        JMSGGroup *deletedGroup = ((JMSGGroup *)(self.conversation.target));
//
//        [deletedGroup exit:^(id resultObject, NSError *error) {
//          [MBProgressHUD hideAllHUDsForView:weakSelf.view animated:YES];
//          if (error == nil) {
//            DDLogDebug(@"推出群组成功");
//            [MBProgressHUD showMessage:@"推出群组成功" view:weakSelf.view];
//            [JMSGConversation deleteGroupConversationWithGroupId:deletedGroup.gid];
//            [self.navigationController popToViewController:self.sendMessageCtl.superViewController animated:YES];
//          } else {
//            DDLogDebug(@"推出群组失败");
//            [MBProgressHUD showMessage:@"推出群组失败" view:weakSelf.view];
//          }
//        }];
//      }
//    }
//      break;
//    default:
//      [MBProgressHUD showMessage:@"更新群组名称" toView:self.view];
//      JMSGGroup *needUpdateGroup = (JMSGGroup *)(self.conversation.target);
//
//      [JMSGGroup updateGroupInfoWithGroupId:needUpdateGroup.gid
//                                       name:[alertView textFieldAtIndex:0].text
//                                       desc:needUpdateGroup.desc
//                          completionHandler:^(id resultObject, NSError *error) {
//                            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
//
//                            if (error == nil) {
//                              [MBProgressHUD showMessage:@"更新群组名称成功" view:self.view];
//                              [self refreshMemberGrid];
//                            } else {
//                              [MBProgressHUD showMessage:@"更新群组名称失败" view:self.view];
//                            }
//                          }];
//      break;
//  }
//  return;
//}


//- (void)createGroupWithAlertView:(UIAlertView *)alertView {
//  {
//    [MBProgressHUD showMessage:@"加好友进群组" toView:self.view];
//    __block JMSGGroup *tmpgroup =nil;
//    typeof(self) __weak weakSelf = self;
//    [JMSGGroup createGroupWithName:@"" desc:@"" memberArray:@[((JMSGUser *)self.conversation.target).username,[alertView textFieldAtIndex:0].text] completionHandler:^(id resultObject, NSError *error) {
//      [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
//      typeof(weakSelf) __strong strongSelf = weakSelf;
//      tmpgroup = (JMSGGroup *)resultObject;
//
//      if (error == nil) {
//        [JMSGConversation createGroupConversationWithGroupId:tmpgroup.gid completionHandler:^(id resultObject, NSError *error) {
//          if (error == nil) {
//            [MBProgressHUD showMessage:@"创建群成功" view:self.view];
//            JMSGConversation *groupConversation = (JMSGConversation *)resultObject;
//            strongSelf.sendMessageCtl.conversation = groupConversation;
//            strongSelf.sendMessageCtl.isConversationChange = YES;
//            [JMessage removeDelegate:strongSelf.sendMessageCtl withConversation:_conversation];
//            [JMessage addDelegate:strongSelf.sendMessageCtl withConversation:groupConversation];
//            strongSelf.sendMessageCtl.targetName = tmpgroup.name;
//            strongSelf.sendMessageCtl.title = tmpgroup.name;
//            [strongSelf.sendMessageCtl setupView];
//            [[NSNotificationCenter defaultCenter] postNotificationName:kConversationChange object:resultObject];
//            [strongSelf.navigationController popViewControllerAnimated:YES];
//          } else {
//            DDLogDebug(@"creategroupconversation error with error : %@",error);
//          }
//        }];
//      } else {
//        [MBProgressHUD showMessage:[JCHATStringUtils errorAlert:error] view:self.view];
//      }
//    }];
//  }
//}
//
//- (void)collectionView:(UICollectionView * _Nonnull)collectionView didSelectItemAtIndexPath:(NSIndexPath * _Nonnull)indexPath {
//  if (indexPath.item == _memberArr.count) {// 添加群成员
//    _isInEditToDeleteMember = NO;
//    [_groupMemberGrip reloadData];
//    UIAlertView *alerView =[[UIAlertView alloc] initWithTitle:@"添加好友进群"
//                                                      message:@"输入好友用户名!"
//                                                     delegate:self
//                                            cancelButtonTitle:@"取消"
//                                            otherButtonTitles:@"确定", nil];
//    alerView.alertViewStyle = UIAlertViewStylePlainTextInput;
//    alerView.tag = kAlertViewTagAddMember;
//    [alerView show];
//    return;
//  }
//
//  if (indexPath.item == _memberArr.count + 1) {// 删除群成员
//    _isInEditToDeleteMember = !_isInEditToDeleteMember;
//    [_groupMemberGrip reloadData];
//    return;
//  }
//
////  点击群成员头像
//  JMSGUser *user = _memberArr[indexPath.item];
//  JMSGGroup *group = _conversation.target;
//  if (_isInEditToDeleteMember) {
//    if ([user.username isEqualToString:group.owner]) {
//      return;
//    }
//    JMSGUser *userToDelete = _memberArr[indexPath.item];
//    [self deleteMemberWithUserName:userToDelete.username];
//  } else {
//    if ([user.username isEqualToString:[JMSGUser myInfo].username]) {
//      JCHATPersonViewController *personCtl =[[JCHATPersonViewController alloc] init];
//      personCtl.hidesBottomBarWhenPushed = YES;
//      [self.navigationController pushViewController:personCtl animated:YES];
//    } else {
//      JCHATFriendDetailViewController *friendCtl = [[JCHATFriendDetailViewController alloc]initWithNibName:@"JCHATFriendDetailViewController" bundle:nil];
//      friendCtl.userInfo = user;
//      friendCtl.isGroupFlag = YES;
//      [self.navigationController pushViewController:friendCtl animated:YES];
//    }
//  }
//  return ;
//}


//- (void)quitGroup {
//  UIAlertView *alerView =[[UIAlertView alloc] initWithTitle:@"退出群聊"
//                                                    message:@""
//                                                   delegate:self
//                                          cancelButtonTitle:@"取消"
//                                          otherButtonTitles:@"确定", nil];
//  alerView.tag = 400;
//  [alerView show];
//  [self removeEditStatus];
//}

// 免打扰设置
- (void)switchDisturb {

  // [MBProgressHUD showMessage:@"正在修改免打扰" toView:self.view];

//  if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//    JMSGUser *jmsgUser = _conversation.target;
//    [jmsgUser setIsNoDisturb:!_isNoDisturb handler:^(id resultObject, NSError *error) {
//      //[MBProgressHUD hideHUDForView:self.view animated:YES];
//      if (error == nil) {
//        _isNoDisturb = !_isNoDisturb;
//        if (jmsgUser.isNoDisturb) {
//          NSLog(@"is no disturb");
//        } else {
//          NSLog(@"is disturb");
//        }
//      }
//    }];
//  } else {
//    JMSGGroup *group = _conversation.target;
//    [group setIsNoDisturb:!_isNoDisturb handler:^(id resultObject, NSError *error) {
//      //[MBProgressHUD hideHUDForView:self.view animated:YES];
//      if (error == nil) {
//        _isNoDisturb = !_isNoDisturb;
//      }
//    }];
//  }
}

#pragma -mark FootTableView Delegate
//- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
//  if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//    return 2;
//  } else {
//    return 4;
//  }
//
//}
//
//- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
//  static NSString *cellIdentifier = @"JCHATFootTableViewCell";
//  JCHATFootTableViewCell *cell = (JCHATFootTableViewCell *)[tableView dequeueReusableCellWithIdentifier:cellIdentifier];
//  if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//    switch (indexPath.row) {
//      case 0:
//        [cell layoutToClearChatRecord];
//        break;
//      case 1:
//        cell.delegate = self;
//        [cell layoutToSetNotifMode:_isNoDisturb];
//        break;
//      default:
//        break;
//    }
//
//  } else {
//    switch (indexPath.row) {
//        //    case 0 为修改 group.name 的 footer suplementary
//      case 0:
//        [cell setDataWithGroupName:((JMSGGroup *)_conversation.target).displayName];
//        break;
//      case 1:
//        [cell layoutToClearChatRecord];
//        break;
//      case 2:
//        cell.delegate = self;
//        [cell layoutToSetNotifMode:_isNoDisturb];
//        break;
//      case 3:
//        cell.delegate = self;
//        [cell layoutToQuitGroup];
//        break;
//      default:
//        break;
//    }
//  }
//    return cell;
//}
//
//- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
//  return 66;
//}
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
//  if (_conversation.conversationType == kJMSGConversationTypeSingle) {
//    [self tapToClearChatRecord];
//  } else {
//    switch (indexPath.row) {
//      case 0:
//        [self tapToEditGroupName];
//        break;
//      case 1:
//        [self tapToClearChatRecord];
//        break;
//      case 2:
//        break;
//      default:
//        break;
//    }
//  }
//}
//
//- (void)didReceiveMemoryWarning {
//  [super didReceiveMemoryWarning];
//}


// 结果回调
- (void)resultCallback:(CDVInvokedUrlCommand*)command pluginResult:(CDVPluginResult*) pluginResult
{
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}


@end
