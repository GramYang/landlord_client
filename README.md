<!-- TOC -->

- [什么是landlord_client](#%e4%bb%80%e4%b9%88%e6%98%aflandlordclient)
- [效果图](#%e6%95%88%e6%9e%9c%e5%9b%be)
- [自定义view](#%e8%87%aa%e5%ae%9a%e4%b9%89view)
  - [CircleImageView](#circleimageview)
  - [CardsPack](#cardspack)
    - [adapter](#adapter)
    - [使用](#%e4%bd%bf%e7%94%a8)
- [网络库](#%e7%bd%91%e7%bb%9c%e5%ba%93)
  - [使用](#%e4%bd%bf%e7%94%a8-1)
  - [流程分析](#%e6%b5%81%e7%a8%8b%e5%88%86%e6%9e%90)
- [游戏逻辑](#%e6%b8%b8%e6%88%8f%e9%80%bb%e8%be%91)
- [与后端的协议适配](#%e4%b8%8e%e5%90%8e%e7%ab%af%e7%9a%84%e5%8d%8f%e8%ae%ae%e9%80%82%e9%85%8d)

<!-- /TOC -->

# 什么是landlord_client
这是一个斗地主游戏的Android前端demo，对应landlord_go后端。

特点
* 使用原生socket+protobuf与后端通信，轻便简捷。
* 牌桌采用自定义view设计，更加简洁（不会套壳）。
* 个人写前端没什么设计天赋，页面比较挫。。

# 效果图
![登陆页面](https://github.com/GramYang/landlord_client/blob/master/pictures/Image%201.png)
![游戏分区](https://github.com/GramYang/landlord_client/blob/master/pictures/Image%202.png)
![游戏大厅](https://github.com/GramYang/landlord_client/blob/master/pictures/Image%204.png)
![准备](https://github.com/GramYang/landlord_client/blob/master/pictures/Image%205.png)
![等待地主出牌](https://github.com/GramYang/landlord_client/blob/master/pictures/Image%206.png)
![加倍](https://github.com/GramYang/landlord_client/blob/master/pictures/Image%207.png)
![出牌](https://github.com/GramYang/landlord_client/blob/master/pictures/Image%208.png)


# 自定义view
## CircleImageView
 [看这里](https://github.com/hdodenhof/CircleImageView)

## CardsPack

继承自recyclerview。

### adapter
其中持有一个CardsPackAdapter，CardsPackAdapter持有两个list代表持有的牌和打出去的牌，牌在点击后从持有的牌中删除并添加到打出去的牌中。

### 使用
在GameActivity中，首先初始化LinearLayoutManager：

```Java
LinearLayoutManager layoutManagerLeft = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,
false);
layoutManagerLeft.setSmoothScrollbarEnabled(false);
```

然后将LinearLayoutManager和传递数据和标记位的CardsPackAdapter传递进CardsPack实例中：

```Java
rivalLeftCardsOut.setLayoutManager(layoutManagerLeft);
leftAdapter = rivalLeftCardsOut.new CardsPackAdapter(this, 1, leftCardsOut);
rivalLeftCardsOut.setAdapter(leftAdapter);
```

# 网络库
网络库在[OkSocket](https://github.com/xuuhaoo/OkSocket)的基础上进行了修改。

## 使用
```Java
ConnectionInfo loginInfo = new ConnectionInfo(Constants.loginHost, Constants.loginPort);
loginManager = OkSocket.open(loginInfo);
loginManager.registerReceiver(new LoginHandler());
loginManager.connect();
```
ConnectionInfo用来存放ip和port，调用OkSocket.open()返回一个IConnectionManager实例，调用registerReceiver()传入连接后的回调handler实例，最后调用connect()进行socket的连接。

* LoginHandler

这是一个SocketActionAdapter的实现类，SocketActionAdapter的实现类会在socket返回信息后分发时逐个遍历，因此可以有多个SocketActionAdapter的实现类去实现相同的方法，他们都会被调用。

## 流程分析
> OkSocket.open(info)

返回一个IConnectionManager实例，该实例以info为key存入一个map缓存，第一次调用的时候调用new ConnectionManagerImpl(info)。ConnectionManagerImpl构造器中设置了一些标记位，提取了ip和port。

> ConnectionManagerImpl.connect()

1. 绑定ActionHandler实例

ActionHandler实现了SocketActionAdapter的三个方法，被存入在一个列表中，在读写线程解析完数据后会使用这个列表来遍历处理数据。如何选择不同的方法来处理不同情况下的数据，就是靠的key了，其实现在ActionDispatcher。
这里的ActionHandler主要处理IOThread启动、断开，连接失败。

2. 绑定DefaultReconnectManager实例

DefaultReconnectManager同样实现了ISocketActionListener，也算是handler的一种。
在attach方法中，持有传入的mConnectionManager，以及其中的mPulseManager（这个时候可能是空的，但是过几毫秒就不是空的了），将DefaultReconnectManager实例也传入到ISocketActionListener列表中。DefaultReconnectManager实现了ISocketActionListener的三个方法，主要实现了断线重连的逻辑。

3. 生成ConnectionManagerImpl.ConnectionThread实例，并Start

其中，socket实例连接指定地址并设置超时，然后生成PulseManager实例，生成IOThreadManager实例并startEngine()。连接成功就发送action_connection_success，抛异常则发送action_connection_failed和异常实例。

4. 生成PulseManager实例

上面生成了PulseManager的实例，其pulse()需要你在设置mSendable后自己手动调用。
在pulse()中，设置好心跳频率后，开启mPulseThread，其线程模式是DUPLEX。run()中，超时了就断开，否则就发送心跳包，发送的动作是通过IConnectionManager实现的。
心跳是在一个线程中定期调用ConnectionManagerImpl的send方法实现的。

5. IOThreadManager.startEngine()

IOThreadManager的构造器持有socket的两个流，以及mActionDispatcher。然后获取mReaderProtocol，也就是ltv协议三个部分长度的解析方法。初始化ReaderImpl和WriterImpl。
startEngine()中生成DuplexWriteThread和DuplexReadThread实例，然后启动。

6. DuplexWriteThread和DuplexReadThread

就是装包和拆包的过程，无限执行WriterImpl和ReaderImpl的write()和read()。

> IConnectionManager.send()

调用IOThreadManager的send()，向WriteImpl的LinkedBlockingQueue中添加消息，WriteImpl的write()则会不停的从LinkedBlockingQueue中取消息处理。

这里增加了一个sendAfterConnect()，用一个信号量控制在connect()中的逻辑执行完后再异步的调用send()

> ReaderImpl.read()

从socket的inputStream中读取流数据后，拆包填充OriginalData，调用
mStateSender.sendBroadcast(IOAction.ACTION_READ_COMPLETE, originalData)
进行分发，这里的mStateSender就是ActionDispatcher。
ActionDispatcher的sendBroadcast()中将action和originalData存入
ActionDispatcher.ActionBean实例，然后存入LinkedBlockingQueue中
ActionDispatcher中的DispatchThread静态实例启动，其run()中从LinkedBlockingQueue取出ActionBean实例，然后遍历ActionDispatcher的mResponseHandlerList（就是上面封装的ISocketActionListener列表）

> ActionDispatcher.dispatchActionToListener()

"action_read_complete"：ReaderImpl.read()解析成功后发送
"action_read_thread_start"：DuplexReadThread的beforeLoop()
"action_write_thread_start"：DuplexWriteThread的beforeLoop()
"action_read_thread_shutdown"：DuplexReadThread的loopFinish()
"action_write_thread_shutdown":DuplexWriteThread的loopFinish()
"action_pulse_request"：WriterImpl的write()
"action_write_complete"：WriterImpl的write()
"action_disconnection"：DisconnectThread运行完之后
"action_connection_success"：ConnectionThread连接成功
"action_connection_failed"：ConnectionThread连接失败抛出异常

# 游戏逻辑

1. 进入房间后准备

准备：发送new ReadyRequest(true)

解除整备：发送new CancelReadyRequest(true)

所有玩家准备后开始抢地主

2. 开始抢地主

onGrabLandlordResponse()中显示三张地主牌，显示手中的牌，显示抢地主面板

选择抢地主：选择加倍

选择不抢地主：顺移到右边的玩家抢地主，如果其他两个玩家都不抢地主，强制成为地主，选择加倍

3. 选择加倍

onEndGrabLandlordResponse()中地主显示加倍选择面板，在地主选择加倍后其他两个玩家选择加倍应答面板

不选择加倍：直接开始游戏

选择加倍：其他玩家根据倍数判断是否加倍，有一个人不同意加倍，则加倍取消，开始游戏

4. 开始游戏

onCardsOutResponse()中出牌和接牌

**对牌的判断和处理逻辑都在前端**

第一个牌出完的玩家触发结束游戏

5. 结束游戏

onEndGameResponse()中弹窗显示战绩，你可以选择继续也可以选择退出房间


# 与后端的协议适配

后端的拆包和封包协议都采用的是ltv格式，length-type-value。内容长度如下：

```Java
public class ReaderProtocol1 implements IReaderProtocol {
    @Override
    public int getHeaderLength() {
        return 2;
    }

    @Override
    public int getTypeLength() {
        return 2;
    }

    @Override
    public int getBodyLength(byte[] header, ByteOrder byteOrder) {
        if (header == null || header.length < getHeaderLength()) {
            return 0;
        }
        ByteBuffer bb = ByteBuffer.wrap(header);
        bb.order(byteOrder);
        return bb.getShort() - 2;
    }
}
```

封包示例

```Java
public class JsonReq implements ISendable {
    private int jsonType;
    private byte[] content;

    public JsonReq(int jsonType, byte[] content) {
        this.jsonType = jsonType;
        this.content = content;
    }

    public int getJsonType() {
        return jsonType;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public byte[] parse() {
        Landlord.JsonREQ.Builder builder = Landlord.JsonREQ.newBuilder();
        builder.setJsonType(jsonType);
        builder.setContent(ByteString.copyFrom(content));
        Landlord.JsonREQ req = builder.build();
        byte[] body = req.toByteArray();
        ByteBuffer bb = ByteBuffer.allocate(4+body.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short)(body.length+2));
        bb.putShort(Constants.JSON_REQ_TYPE);
        bb.put(body);
        return bb.array();
    }
}
```

**特别注意：后端采用的是小端序列，因此前端也要用小端序列，而Java默认的是大端序列。**
