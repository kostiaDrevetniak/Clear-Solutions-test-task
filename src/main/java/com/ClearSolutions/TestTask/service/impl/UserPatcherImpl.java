package com.ClearSolutions.TestTask.service.impl;

import com.ClearSolutions.TestTask.model.User;
import com.ClearSolutions.TestTask.service.Patcher;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;

@Service
public class UserPatcherImpl implements Patcher<User> {

    @SneakyThrows // IllegalAccessException
    @Override
    public void patch(User existed, User incoming) {
        Class<?> userClass = User.class;
        Field[] fields = userClass.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            Object value = field.get(incoming);
            if (value != null) {
                field.set(existed, value);
            }

            field.setAccessible(false);
        }
    }
}
