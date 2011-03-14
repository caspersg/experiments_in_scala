import static org.junit.Assert.assertEquals;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

public class JavaAtom {
    
    @Test
    public void test() {
        AtomicReference<Integer> atom = new AtomicReference<Integer>(1);
        while (true) {
            Integer previous = atom.get();
            Integer update = previous + 2;
            if (atom.compareAndSet(previous, update)) {
                break;
            }
        }
        assertEquals(3, atom.get().intValue());
    }
}
