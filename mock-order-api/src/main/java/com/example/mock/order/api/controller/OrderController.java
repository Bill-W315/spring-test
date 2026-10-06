ackage com.example.mock.order.api.controller;

import com.example.mock.order.operation.OrderOperationHandler;
import com.example.mock.order.view.swagger.api.OrderApi;
import com.example.mock.order.view.swagger.model.OrderRequestBody;
import com.example.mock.order.view.swagger.model.OrderResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class OrderController implements OrderApi {

    @Autowired
    private OrderOperationHandler orderOperationHandler;

    @Override
    public ResponseEntity<OrderResponseBody> ordersPost(@Valid @RequestBody OrderRequestBody body) {
        return orderOperationHandler.execute(body.getOpCode().toString(), body);
    }
}
