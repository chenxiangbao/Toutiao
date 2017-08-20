package com.nowcoder.async;

import java.util.List;

public interface EventHandle {
    void deHandle(EventModel model);
    //观察者模式
    List<EventType> getSupportEventTypes();
}
