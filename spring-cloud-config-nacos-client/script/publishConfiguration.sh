#!/usr/bin/env bash

# 配置发布 ${prefix}-${spring.profile.active}.${file-extension}
# nacos-config-client-app.properties
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos-config-client-app.properties&group=DEFAULT_GROUP&content=user.id=1%0Auser.name=Arvin%0Auser.age=25"
# nacos-config-client-app-dev.properties 开发环境
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos-config-client-app-dev.properties&group=DEFAULT_GROUP&content=user.id=2%0Auser.name=Bob%0Auser.age=26"
# nacos-config-client-app-test.properties 测试环境
curl -X POST "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=nacos-config-client-app-test.properties&group=DEFAULT_GROUP&content=user.id=3%0Auser.name=Cindy%0Auser.age=27"
