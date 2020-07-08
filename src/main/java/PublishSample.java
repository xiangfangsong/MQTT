import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class PublishSample {
    private static void send(String broker, String topic, String content){
        int qos = 1; //保证消息接收者至少会收到一次，可能造成消息重复
        String userName = "test";
        String password = "test";
        String clientId = "pubClient";
        try {
            MqttClient sampleClient = new MqttClient(broker, clientId, new MemoryPersistence());  // 创建客户端
            MqttConnectOptions connOpts = new MqttConnectOptions(); // 创建链接参数
            connOpts.setCleanSession(false); // 在重新启动和重新连接时记住状态
            connOpts.setUserName(userName);  // 设置连接的用户名
            connOpts.setPassword(password.toCharArray());
            sampleClient.connect(connOpts); // 建立连接
            MqttMessage message = new MqttMessage(content.getBytes());  // 创建消息
            message.setQos(qos);   // 设置消息的服务质量
            sampleClient.publish(topic, message);  // 发布消息
            sampleClient.disconnect(); // 断开连接
            sampleClient.close(); // 关闭客户端
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();
        }
    }

    public static void main(String[] args) {
       send("tcp://127.0.0.1:1883","myMQTT/info","这是发送的消息1234");
    }
}
