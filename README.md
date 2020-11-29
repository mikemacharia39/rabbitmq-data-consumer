# RabbitMQ Data Consumer

* This project shows how to work with rabbitmq to process data asynchronously

### Getting started

* The application shows how to work with RabbitMQ to consumer messages from  a queue
* Ensure you have installed the below applications

    |Application|Version|
    |---------|------------|
    |Erlang|10.5|
    |RabbitMQ|3.8.1|
    |JDK| \> 8 |

* Publish the message below to your queue

    `{
        "employeeName": "Mikehenry", 
        "salary": 130999, 
        "age": 39
    }`


### Application flows and processes

* RabbitMQConfiguration - Responsible for loading RabbitMQ run configurations
    - `cachingConnectionFactory()` - Uses the `CachingConnectionFactory` loads rabbitmq configurations. Caching is important since it adds sessions to the ConnectionFactory. To learn more read. To learn more read  https://docs.oracle.com/javaee/7/api/javax/jms/Session.html?is-external=true
    - `simpleMessageListenerContainer()` - Create a listener container from the ConnectionFactory
* DataConsumer - Responsible for receiving messages from queue and processing them
    - `onMessage` - An inbuilt method within the `ChannelAwareMessageListener` that allows the implementation to be aware of the channel from which the message was received
    
### Other Materials

Big thanks to **Dummy Rest Api Example** for exposing API the project used for HTTP request processing.
    
    * http://dummy.restapiexample.com/ 
 