    package threading;
    import java.util.LinkedList;
    import java.util.Queue;
    import java.util.concurrent.Semaphore;

        public class ProducerConsumerSemaphore {
            private static final int QUEUE_CAPACITY = 5;
            private static final Queue<Integer> sharedQueue = new LinkedList<>();
            private static final Semaphore emptySlots = new Semaphore(QUEUE_CAPACITY);
            private static final Semaphore fullSlots = new Semaphore(0);
            private static final Semaphore mutex = new Semaphore(1);

            public static void main(String[] args) {
                Thread producer = new Thread(new Producer(), "Producer");
                Thread consumer = new Thread(new Consumer(), "Consumer");

                producer.start();
                consumer.start();
            }
            static class Producer implements Runnable {
                @Override
                public void run() {
                    int item = 0;
                    while (true) {
                        try {
                            emptySlots.acquire();
                            mutex.acquire();
                            sharedQueue.add(item);
                            System.out.println("Produced: " + item);
                            item++;
                            mutex.release();
                            fullSlots.release();
                            Thread.sleep(1000); // Simulate time taken to produce an item
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            static class Consumer implements Runnable {
                @Override
                public void run() {
                    while (true) {
                        try {
                            fullSlots.acquire();
                            mutex.acquire();
                            int item = sharedQueue.poll();
                            System.out.println("Consumed: " + item);
                            mutex.release();
                            emptySlots.release();
                            Thread.sleep(1500); // Simulate time taken to consume an item
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                    }
                }
            }
        }

    }
