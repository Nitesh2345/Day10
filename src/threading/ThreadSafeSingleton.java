package threading;
import java.util.LinkedList;
import java.util.Queue;

    public class ThreadSafeSingleton {
        private static ThreadSafeSingleton instance;
        private Queue<String> sharedQueue;

        private ThreadSafeSingleton() {
            sharedQueue = new LinkedList<>();
        }

        public static synchronized ThreadSafeSingleton getInstance() {
            if (instance == null) {
                instance = new ThreadSafeSingleton();
            }
            return instance;
        }

        public synchronized void addToQueue(String item) {
            sharedQueue.add(item);
        }

        public synchronized String removeFromQueue() {
            return sharedQueue.poll();
        }

        public synchronized int getQueueSize() {
            return sharedQueue.size();
        }

        public static void main(String[] args) {
            ThreadSafeSingleton singleton = ThreadSafeSingleton.getInstance();

            // Create threads to demonstrate thread-safe access to the shared queue
            Thread producer = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    singleton.addToQueue("Item " + i);
                    System.out.println("Added: Item " + i);
                    try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            });

            Thread consumer = new Thread(() -> {
                for (int i = 0; i < 10; i++) {
                    String item = singleton.removeFromQueue();
                    System.out.println("Removed: " + item);
                    try { Thread.sleep(150); } catch (InterruptedException e) { e.printStackTrace(); }
                }
            });

            producer.start();
            consumer.start();

            try {
                producer.join();
                consumer.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("Final queue size: " + singleton.getQueueSize());
        }
    }


