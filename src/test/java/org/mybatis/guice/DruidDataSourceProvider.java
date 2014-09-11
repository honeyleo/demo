package org.mybatis.guice;

import java.util.Properties;

import javax.inject.Inject;
import javax.sql.DataSource;

import com.google.inject.Provider;

public class DruidDataSourceProvider implements Provider<DataSource> {

	private DataSource dataSource;
	
	@Inject
	public DruidDataSourceProvider(Properties properties) {
		try {
			dataSource = com.alibaba.druid.pool.DruidDataSourceFactory.createDataSource(properties);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public DataSource get() {
		return dataSource;
	}

}
