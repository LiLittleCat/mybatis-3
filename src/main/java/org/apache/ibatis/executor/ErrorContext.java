/**
 *    Copyright 2009-2020 the original author or authors.
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
package org.apache.ibatis.executor;

/**
 * @see https://blog.csdn.net/lqzkcx3/article/details/84944285
 * @author Clinton Begin
 */
public class ErrorContext {
  // 将执行上下文信息的收集独立出来并集中到一处
  private static final String LINE_SEPARATOR = System.lineSeparator();
  // 正是因为使用了ThreadLocal<T>, 我们就能直接取到之前执行本SQL的线程上的信息, 也就很方便的构建出异常发生时的上下文，快速排错。
  private static final ThreadLocal<ErrorContext> LOCAL = ThreadLocal.withInitial(ErrorContext::new);

  private ErrorContext stored;
  private String resource;
  private String activity;
  private String object;
  private String message;
  private String sql;
  private Throwable cause;

  private ErrorContext() {
  }

  public static ErrorContext instance() {
    return LOCAL.get();
  }

  public ErrorContext store() {
    ErrorContext newContext = new ErrorContext();
    newContext.stored = this;
    LOCAL.set(newContext);
    return LOCAL.get();
  }

  public ErrorContext recall() {
    if (stored != null) {
      LOCAL.set(stored);
      stored = null;
    }
    return LOCAL.get();
  }

  public ErrorContext resource(String resource) {
    this.resource = resource;
    return this;
  }

  public ErrorContext activity(String activity) {
    this.activity = activity;
    return this;
  }

  public ErrorContext object(String object) {
    this.object = object;
    return this;
  }

  public ErrorContext message(String message) {
    this.message = message;
    return this;
  }

  public ErrorContext sql(String sql) {
    this.sql = sql;
    return this;
  }

  public ErrorContext cause(Throwable cause) {
    this.cause = cause;
    return this;
  }

  public ErrorContext reset() {
    resource = null;
    activity = null;
    object = null;
    message = null;
    sql = null;
    cause = null;
    LOCAL.remove();
    return this;
  }

  @Override
  public String toString() {
    StringBuilder description = new StringBuilder();

    // message
    if (this.message != null) {
      description.append(LINE_SEPARATOR);
      description.append("### ");
      description.append(this.message);
    }

    // resource
    if (resource != null) {
      description.append(LINE_SEPARATOR);
      description.append("### The error may exist in ");
      description.append(resource);
    }

    // object
    if (object != null) {
      description.append(LINE_SEPARATOR);
      description.append("### The error may involve ");
      description.append(object);
    }

    // activity
    if (activity != null) {
      description.append(LINE_SEPARATOR);
      description.append("### The error occurred while ");
      description.append(activity);
    }

    // sql
    if (sql != null) {
      description.append(LINE_SEPARATOR);
      description.append("### SQL: ");
      description.append(sql.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ').trim());
    }

    // cause
    if (cause != null) {
      description.append(LINE_SEPARATOR);
      description.append("### Cause: ");
      description.append(cause.toString());
    }

    return description.toString();
  }

}
