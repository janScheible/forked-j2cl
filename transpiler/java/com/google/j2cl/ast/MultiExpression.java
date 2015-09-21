/*
 * Copyright 2015 Google Inc.
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

import com.google.common.base.Preconditions;
import com.google.j2cl.ast.processors.Visitable;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents multiple ordered expressions as a single compound expression.
 * <p>
 * They are executed in order and the value of the last one is returned.
 */
@Visitable
public class MultiExpression extends Expression {
  @Visitable List<Expression> expressions = new ArrayList<>();

  public MultiExpression(List<Expression> expressions) {
    Preconditions.checkNotNull(expressions);
    this.expressions.addAll(expressions);
  }

  public List<Expression> getExpressions() {
    return expressions;
  }

  @Override
  public TypeDescriptor getTypeDescriptor() {
    Preconditions.checkArgument(!expressions.isEmpty());
    return expressions.get(expressions.size() - 1).getTypeDescriptor();
  }

  @Override
  public Node accept(Processor processor) {
    return Visitor_MultiExpression.visit(processor, this);
  }
}