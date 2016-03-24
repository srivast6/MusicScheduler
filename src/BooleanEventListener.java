import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.List;

/**
 * This class uses the EventQueue to process its events, but you should only really do this if the
 * changes you make have an impact on part of a GUI eg. adding a button to a JFrame.
 *
 * Otherwise, you should create your own event dispatch thread that can handle change events
 */
public class BooleanEventListener implements BooleanChangeDispatcher {

  public static void main(String[] args) {

    // required in the object
    BooleanChangeListener listener = new BooleanChangeListener() {
      @Override
      public void stateChanged(BooleanChangeEvent event) {
        System.out.println("Detected change to: " + event.getDispatcher().getFlag() + " -- event: "
            + event);
      }
    };

    BooleanEventListener test = new BooleanEventListener(false);
    test.addBooleanChangeListener(listener);

    test.setFlag(false); // no change, no event dispatch
    test.setFlag(true); // changed to true -- event dispatched

  }

  private boolean flag;
  private List<BooleanChangeListener> listeners;

  public BooleanEventListener(boolean initialFlagState) {
    flag = initialFlagState;
    listeners = new ArrayList<BooleanChangeListener>();
  }

  @Override
  public void addBooleanChangeListener(BooleanChangeListener listener) {
    listeners.add(listener);
  }

  @Override
  public void setFlag(boolean flag) {
    if (this.flag != flag) {
      this.flag = flag;
      dispatchEvent();
    }
  }

  @Override
  public boolean getFlag() {
    return flag;
  }

  private void dispatchEvent() {
    final BooleanChangeEvent event = new BooleanChangeEvent(this);
    for (BooleanChangeListener l : listeners) {
      dispatchRunnableOnEventQueue(l, event);
    }
  }

  private void dispatchRunnableOnEventQueue(final BooleanChangeListener listener,
      final BooleanChangeEvent event) {

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        listener.stateChanged(event);
      }
    });
  }

}