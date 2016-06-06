/*
 * Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.j2cl.ast;

import com.google.common.collect.Lists;
import com.google.j2cl.ast.processors.Visitable;

import java.util.List;

/**
 * Class for an inline (lambda) function expression.
 */
@Visitable
public class FunctionExpression extends Expression {
  @Visitable Block body;
  @Visitable final List<Variable> parameters;
  private final TypeDescriptor typeDescriptor;

  @Override
  public TypeDescriptor getTypeDescriptor() {
    return typeDescriptor;
  }

  public List<Variable> getParameters() {
    return parameters;
  }

  public Statement getBody() {
    return body;
  }

  public FunctionExpression(
      TypeDescriptor typeDescriptor, List<Variable> parameters, List<Statement> statements) {
    this.parameters = Lists.newArrayList(parameters);
    this.body = new Block(statements);
    this.typeDescriptor = typeDescriptor;
  }

  @Override
  public Node accept(Processor processor) {
    return Visitor_FunctionExpression.visit(processor, this);
  }
}
