import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class SubscribeSample {
    private static void subscribe(String HOST, String TOPIC){
        int qos = 1; //保证消息接收者至少会收到一次，可能造成消息重复
        String clientid = "subClient3";
        String userName = "test";
        String passWord = "test";
        try {
            // host为主机名，test为clientid即连接MQTT的客户端ID，一般以客户端唯一标识符表示，MemoryPersistence设置clientid的保存形式，默认为以内存保存
            MqttClient client = new MqttClient(HOST, clientid, new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions(); // MQTT的连接设置
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            options.setUserName(userName);  // 设置连接的用户名
            options.setPassword(passWord.toCharArray()); // 设置连接的密码
            options.setConnectionTimeout(10);  // 设置超时时间 单位为秒
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
            options.setKeepAliveInterval(20);
            client.setCallback(new MqttCallback() { // 设置回调函数
                public void connectionLost(Throwable cause) {
                    System.out.println("connectionLost");
                    cause.printStackTrace();
                }

                public void messageArrived(String topic, MqttMessage message) {
                    String content = new String(message.getPayload());
                    System.out.println("topic:"+topic);
                    System.out.println("Qos:"+message.getQos());
                    System.out.println("收到的消息:"+content);
                }

                public void deliveryComplete(IMqttDeliveryToken token) {
                    System.out.println("deliveryComplete---------"+ token.isComplete());
                }
            });
            client.connect(options);
            client.subscribe(TOPIC, qos);  //订阅消息
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        subscribe("tcp://127.0.0.1:1883","myMQTT/info");
    }
}
