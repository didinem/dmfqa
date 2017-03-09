package org.didinem.handle;

import java.util.List;

/**
 * Created by didinem on 3/5/2017.
 */
public interface CacheHandler {

    void set(String key, String value);

    String get(String key);

    void push(String key, String ...value);

    void push(String key, List<String> list);

    List<Object> getList(String key);

    boolean isExistKey(String key);

    void delete(String key);

}
