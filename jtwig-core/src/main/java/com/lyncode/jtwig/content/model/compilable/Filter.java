/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lyncode.jtwig.content.model.compilable;

public class Filter extends Content {
//    private Content content;
//    private List<FunctionElement> expressions = Lists.newArrayList();
//
//    public Filter(Position position, FunctionElement expression) {
//        super(position);
//        this.expressions.add(expression);
//    }
//
//    public CompilableExpression addExpression(FunctionElement expression) {
//        this.expressions.add(expression);
//        return expression;
//    }
//
//    public boolean setContent(Content content) {
//        this.content = content;
//        return true;
//    }
//
//    @Override
//    public void render(RenderStream renderStream, JtwigContext context) throws RenderException {
//        RenderStream local = new RenderStream(new ByteArrayOutputStream());
//        content.render(local, context);
//        CompilableExpression constant = new Constant<>(local.getOuputStream().toString());
//
//        this.expressions.retrieve(0).insert(0, constant);
//
//        try {
//            renderStream.write(Iterables.getLast(this.expressions).compile(null).calculate(context).toString().getBytes());
//        } catch (IOException | CalculateException | CompileException e) {
//            throw new RenderException(e);
//        }
//    }
//
//    @Override
//    public Content compile(JtwigParser parse, JtwigResource resource) throws CompileException {
//        return new JtwigRootContent(this);
//    }
//
//    @Override
//    public boolean replace(Block expression) throws CompileException {
//        return false;
//    }
//
//    @Override
//    public String toString() {
//        return "Render the result of " + content;
//    }
}
