package org.mybatis.guice;

import java.util.Properties;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidDataSourceFactory implements org.apache.ibatis.datasource.DataSourceFactory {

	private DruidDataSource dataSource = new DruidDataSource();
	
	@Override
	public void setProperties(Properties props) {
		dataSource.configFromPropety(props);
	}

	@Override
	public DataSource getDataSource() {
		return dataSource;
	}

}
