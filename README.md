
## 中间件服务准备

### 环境变量配置 
`NAMESRV_ADDR` -> `127.0.0.1:9876`

### 启动服务
```
mqnamesrv -n 127.0.0.1:9876
mqbroker -n 127.0.0.1:9876
```

## 实例

### Simple Example

#### SyncProducer

> 同步传输广泛应用于重要的通知消息，短信通知，短信营销系统等

运行 SyncProducer
```
run kvn.practices.simple.SyncProducer.main()
```

查看主题状态、命令行打印消息
```
> mqadmin topicStatus -t SyncTopic1
> mqadmin printMsg -t SyncTopic1
```
#### AsyncProducer

> 异步传输常用于响应时间敏感的业务场景

运行 AsyncProducer
```
run kvn.practices.simple.AsyncProducer.main()
```

命令行打印消息
```
> mqadmin printMsg -t AsyncTopic1
```

#### OnewayProducer

> 单向传输用于中等可靠的情况，例如日志收集等

运行 OnewayProducer
```
run kvn.practices.simple.OnewayProducer.main()
```

命令行打印消息
```
> mqadmin printMsg -t OnewayTopic1
```

### Order Example

> RocketMQ 提供 FIFO 原则的有序消息。

运行 OrderedProducer 发送消息

```
run kvn.practices.order.OrderedProducer.main()
```

运行 OrderedConsumer 接收消息

```
run kvn.practices.order.OrderedConsumer.main()
```

### Broadcasting Example

> 广播，顾名思义是给某个主题的所有订阅者发送消息。

运行 BroadcastConsumer 接收消息（可启动多个查看广播效果）

```
run kvn.practices.broadcast.BroadcastConsumer.main()
```

运行 BroadcastProducer 发送消息

```
run kvn.practices.broadcast.BroadcastProducer.main()
```

### Schedule Example

> 计划消息不同于正常消息，在它未接收到发送的时间计划前，它不会传输任何消息。

运行 ScheduleProducer 发送消息

```
run kvn.practices.schedule.ScheduleProducer.main()
```

运行 ScheduleConsumer 接收消息

```
run kvn.practices.schedule.ScheduleConsumer.main()
```

### Batch Example

> 批量发送消息可改善发送小消息的性能。
>
> 同一批次的消息具有：相同的主题，相同的 waitStoreMsgOK 且不支持计划消息。另外，同一批次的消息大小不超过 1MiB。

运行 BatchProducer 批量发送消息

```
run kvn.practices.batch.BatchProducer.main()
```

打印消息

```
> mqadmin printMsg -t BatchTopic1
```

### Filter Example

> 只有消费者可以通过 SQL92 实现消息选择。接口是：
>
> public void subscribe(final String topic, final MessageSelector messageSelector)

> **在本例中, Broker 默认并未开启PropertyFilter，通过以下方式开启过滤：**
> - 启动时
> `mqbroker -c broker.conf`， 其中 `broker.conf` 为配置文件名称， 且包含配置项 `enablePropertyFilter=true`。
> - 运行时  
> `mqadmin updateBrokerConfig -c DefaultCluster -k key1 -v value1`，其中 `DefaultCluster` 为 ClusterName， `key1` 为属性名， `value1` 为属性值。
>
> 最后，通过 `mqadmin getBrokerConfig -c DefaultCluster` 查看 BrokerConfig，其中 `DefaultCluster` 为 ClusterName。
  


运行 FilterProducer 发送消息

```
> run kvn.practices.filter.FilterProducer.main()
```

运行 FilterConsumer 接收消息

```
> run kvn.practices.filter.FilterConsumer.main()
```

### Logappender 

> 目前支持 log4j、 log4j2、 logback 日志系统。

官网示例：http://rocketmq.apache.org/docs/logappender-example/

### OpenMessaging Example

> OpenMessaging 是面向云的，供应商中立的分布式消息开放标准。

运行 OMSProducer 发送消息，包含 Sync, Async, Oneway 三种方式。
```
> run kvn.practices.openmessaging.OMSProducer.main()
```

运行 OMSPullConsumer 或 OMSPushConsumer 接收消息
```
> run kvn.practices.openmessaging.OMSPullConsumer.main()
```
or 
```
> run kvn.practices.openmessaging.OMSPushConsumer.main()
```

## 结束
更多应用及配置请查看官网。