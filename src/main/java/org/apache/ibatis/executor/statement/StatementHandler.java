/**
 *    Copyright 2009-2016 the original author or authors.
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
package org.apache.ibatis.executor.statement;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.ibatis.cursor.Cursor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.session.ResultHandler;

/**
 * 语句处理器，对应 JDBC 中的 Statement
 *
 * 参数处理，结果处理
 *
 * @author Clinton Begin
 */
public interface StatementHandler {

  // 基于连接创建 Statement
  Statement prepare(Connection connection, Integer transactionTimeout)
      throws SQLException;
  // 预处理参数
  void parameterize(Statement statement)
      throws SQLException;
  // 批处理
  void batch(Statement statement)
      throws SQLException;
  // 修改
  int update(Statement statement)
      throws SQLException;
  // 查询，结果给 ResultHandler
  <E> List<E> query(Statement statement, ResultHandler resultHandler)
      throws SQLException;
  // 查询游标
  <E> Cursor<E> queryCursor(Statement statement)
      throws SQLException;
  // 获取动态 SQL
  BoundSql getBoundSql();
  // 获取参数处理器
  ParameterHandler getParameterHandler();

}
