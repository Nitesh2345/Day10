package threading;

public class LivelockExample {
        static class Resource {
            private final String name;

            public Resource(String name) {
                this.name = name;
            }

            public String getName() {
                return name;
            }

            public synchronized void tryLock(Resource other) {
                while (true) {
                    if (this.tryAcquireLock()) {
                        if (other.tryAcquireLock()) {
                            System.out.println(Thread.currentThread().getName() + ": Locked " + this.getName() + " and " + other.getName());
                            return;
                        } else {
                            this.releaseLock();
                        }
                    }
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                }
            }

            private boolean tryAcquireLock() {
                // Simulate lock acquisition
                return true;
            }

            private void releaseLock() {
                // Simulate lock release
            }
        }

        public static void main(String[] args) {
            final Resource resource1 = new Resource("Resource1");
            final Resource resource2 = new Resource("Resource2");

            Thread thread1 = new Thread(() -> resource1.tryLock(resource2), "Thread 1");
            Thread thread2 = new Thread(() -> resource2.tryLock(resource1), "Thread 2");

            thread1.start();
            thread2.start();
        }
    }
