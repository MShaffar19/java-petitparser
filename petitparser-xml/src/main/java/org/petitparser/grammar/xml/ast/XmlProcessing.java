package org.petitparser.grammar.xml.ast;

import org.petitparser.grammar.xml.XmlGrammar;

import java.util.Objects;

/**
 * XML processing instruction.
 */
public class XmlProcessing extends XmlData {

  private final String target;

  public XmlProcessing(String target, String data) {
    super(data);
    this.target = target;
  }

  public String getTarget() {
    return target;
  }

  @Override
  public void writeTo(StringBuffer buffer) {
    buffer.append(XmlGrammar.OPEN_PROCESSING);
    buffer.append(getTarget());
    if (!getData().isEmpty()) {
      buffer.append(XmlGrammar.WHITESPACE);
      buffer.append(getData());
    }
    buffer.append(XmlGrammar.CLOSE_PROCESSING);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj) || getClass() != obj.getClass()) {
      return false;
    }
    XmlProcessing other = (XmlProcessing) obj;
    return target.equals(other.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), getTarget());
  }
}
