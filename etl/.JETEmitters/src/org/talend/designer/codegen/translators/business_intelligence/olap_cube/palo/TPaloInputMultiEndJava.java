package org.talend.designer.codegen.translators.business_intelligence.olap_cube.palo;

public class TPaloInputMultiEndJava
{
  protected static String nl;
  public static synchronized TPaloInputMultiEndJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TPaloInputMultiEndJava result = new TPaloInputMultiEndJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = NL + "\t}" + NL + "}";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    return stringBuffer.toString();
  }
}
