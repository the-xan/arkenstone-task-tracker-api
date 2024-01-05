package the.xan.arkenstone.task.tracker.api.services;

import org.springframework.stereotype.Service;

public interface MyTestService {
    void checkOrder();
}

@Service
class MyTestServiceImpl implements MyTestService {

    @Override
    public void checkOrder() {

    }
}
