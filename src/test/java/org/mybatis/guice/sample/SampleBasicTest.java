/*
 *    Copyright 2010-2012 The MyBatis Team
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.guice.sample;

import static com.google.inject.Guice.createInjector;
import static com.google.inject.name.Names.bindProperties;
import static org.apache.ibatis.io.Resources.getResourceAsReader;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.junit.Before;
import org.junit.Test;
import org.mybatis.guice.CustomException;
import org.mybatis.guice.DruidDataSourceProvider;
import org.mybatis.guice.MyBatisModule;
import org.mybatis.guice.datasource.helper.JdbcHelper;
import org.mybatis.guice.sample.domain.User;
import org.mybatis.guice.sample.mapper.UserMapper;
import org.mybatis.guice.sample.service.FooService;
import org.mybatis.guice.sample.service.FooServiceMapperImpl;

import com.google.inject.Injector;

/**
 * Example of MyBatis-Guice basic integration usage.
 *
 * This is the recommended scenario.
 *
 * @version $Id$
 */
public class SampleBasicTest {

    private Injector injector;

    private FooService fooService;

    @Before
    public void setupMyBatisGuice() throws Exception {

        // bindings
        this.injector = createInjector(new MyBatisModule() {

                    @Override
                    protected void initialize() {
//                        install(JdbcHelper.MySQL);
                        bind(Properties.class).toInstance(createProperties());
                        bindDataSourceProviderType(DruidDataSourceProvider.class);
                        bindTransactionFactoryType(JdbcTransactionFactory.class);
                        addMapperClass(UserMapper.class);

                        bindProperties(binder(), createProperties());
                        bind(FooService.class).to(FooServiceMapperImpl.class);
                    }

                }
        );

        // prepare the test db
//        Environment environment = this.injector.getInstance(SqlSessionFactory.class).getConfiguration().getEnvironment();
//        DataSource dataSource = environment.getDataSource();
        DataSource dataSource = this.injector.getInstance(DataSource.class);
        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
        runner.setAutoCommit(true);
        runner.setStopOnError(true);
        runner.runScript(getResourceAsReader("org/mybatis/guice/sample/db/database-schema.sql"));
        runner.runScript(getResourceAsReader("org/mybatis/guice/sample/db/database-test-data.sql"));
        runner.closeConnection();

        this.fooService = this.injector.getInstance(FooService.class);
    }

    protected static Properties createTestProperties() {
        Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "development");
        myBatisProperties.setProperty("JDBC.username", "root");
        myBatisProperties.setProperty("JDBC.password", "root");
        myBatisProperties.setProperty("JDBC.schema", "car-server");
        myBatisProperties.setProperty("JDBC.autoCommit", "false");
        return myBatisProperties;
    }

    /**
     * <property name="url" value="${jdbc.url}" />
	    <property name="username" value="${jdbc.username}" />
	    <property name="password" value="${jdbc.password}" />
	    <property name="initialSize" value="${jdbc.initialSize}" />
	    <property name="maxActive" value="${jdbc.maxActive}" />
     * @return
     */
    protected static Properties createProperties() {
		Properties properties = new Properties();
		properties.setProperty("mybatis.environment.id", "development");
		properties.setProperty("url", "jdbc:mysql://localhost:3306/test?useUnicode=true&amp;autoReconnect=true&amp;characterEncoding=utf8");
		properties.setProperty("username", "root");
		properties.setProperty("password", "root");
		properties.setProperty("initialSize", "2");
		properties.setProperty("maxActive", "10");
		return properties;
	}
    @Test
    public void testFooService(){
        User user = this.fooService.doSomeBusinessStuff("u1");
        assertNotNull(user);
        assertEquals("Pocoyo", user.getName());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testTransactionalOnClassAndMethod() {
    	User user = new User();
    	user.setName("Christian Poitras");
        this.fooService.brokenInsert(user);
    }
    
    @Test(expected=CustomException.class)
    public void testTransactionalOnClass() {
    	User user = new User();
    	user.setName("Christian Poitras");
        this.fooService.brokenInsert2(user);
    }
}
