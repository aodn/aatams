package org.talend.designer.codegen.translators.processing;

import org.talend.core.model.process.INode;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import java.util.Map;

public class TAggregateSortedRowMainJava
{
  protected static String nl;
  public static synchronized TAggregateSortedRowMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TAggregateSortedRowMainJava result = new TAggregateSortedRowMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = "currentRowIndex_";
  protected final String TEXT_3 = "++;";
  protected final String TEXT_4 = NL + "boolean sameGroup_";
  protected final String TEXT_5 = " = true;";
  protected final String TEXT_6 = "if(flag_";
  protected final String TEXT_7 = "){" + NL + "\tflag_";
  protected final String TEXT_8 = " = false;";
  protected final String TEXT_9 = NL + "\t\t\tgroup_";
  protected final String TEXT_10 = "_";
  protected final String TEXT_11 = " = ";
  protected final String TEXT_12 = ".";
  protected final String TEXT_13 = ".clone();";
  protected final String TEXT_14 = NL + "\t\t\tgroup_";
  protected final String TEXT_15 = "_";
  protected final String TEXT_16 = " = ";
  protected final String TEXT_17 = ".";
  protected final String TEXT_18 = ";";
  protected final String TEXT_19 = NL;
  protected final String TEXT_20 = "_";
  protected final String TEXT_21 = "_";
  protected final String TEXT_22 = "_";
  protected final String TEXT_23 = " = ";
  protected final String TEXT_24 = ".";
  protected final String TEXT_25 = ";";
  protected final String TEXT_26 = "\tif(";
  protected final String TEXT_27 = ".";
  protected final String TEXT_28 = " != null){";
  protected final String TEXT_29 = "count_";
  protected final String TEXT_30 = "_";
  protected final String TEXT_31 = "_";
  protected final String TEXT_32 = " = 1;";
  protected final String TEXT_33 = "\t}else{" + NL + "count_";
  protected final String TEXT_34 = "_";
  protected final String TEXT_35 = "_";
  protected final String TEXT_36 = " = 0;" + NL + "}";
  protected final String TEXT_37 = "\tif(";
  protected final String TEXT_38 = ".";
  protected final String TEXT_39 = " != null){";
  protected final String TEXT_40 = NL + "\t\t";
  protected final String TEXT_41 = "_";
  protected final String TEXT_42 = "_";
  protected final String TEXT_43 = "_";
  protected final String TEXT_44 = " = (double)";
  protected final String TEXT_45 = ".";
  protected final String TEXT_46 = ";";
  protected final String TEXT_47 = "\t}else{" + NL + "\t\t";
  protected final String TEXT_48 = "_";
  protected final String TEXT_49 = "_";
  protected final String TEXT_50 = "_";
  protected final String TEXT_51 = " = (double)0;" + NL + "\t}";
  protected final String TEXT_52 = "\tif(";
  protected final String TEXT_53 = ".";
  protected final String TEXT_54 = " != null){";
  protected final String TEXT_55 = NL + "\t\t";
  protected final String TEXT_56 = "_";
  protected final String TEXT_57 = "_";
  protected final String TEXT_58 = "_";
  protected final String TEXT_59 = " = ";
  protected final String TEXT_60 = ".";
  protected final String TEXT_61 = ";";
  protected final String TEXT_62 = "\t}else{" + NL + "\t\t";
  protected final String TEXT_63 = "_";
  protected final String TEXT_64 = "_";
  protected final String TEXT_65 = "_";
  protected final String TEXT_66 = " = new BigDecimal(\"0.0\");" + NL + "\t}";
  protected final String TEXT_67 = NL + "if(";
  protected final String TEXT_68 = ".";
  protected final String TEXT_69 = " != null){" + NL + "\tsum_";
  protected final String TEXT_70 = "_";
  protected final String TEXT_71 = "_";
  protected final String TEXT_72 = " = (double)";
  protected final String TEXT_73 = ".";
  protected final String TEXT_74 = ";" + NL + "\tcount_";
  protected final String TEXT_75 = "_";
  protected final String TEXT_76 = "_";
  protected final String TEXT_77 = " = 1;" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_78 = "_";
  protected final String TEXT_79 = "_";
  protected final String TEXT_80 = " = (double)0;" + NL + "\tcount_";
  protected final String TEXT_81 = "_";
  protected final String TEXT_82 = "_";
  protected final String TEXT_83 = " = 0;" + NL + "}";
  protected final String TEXT_84 = NL + "if(";
  protected final String TEXT_85 = ".";
  protected final String TEXT_86 = " != null){" + NL + "\tsum_";
  protected final String TEXT_87 = "_";
  protected final String TEXT_88 = "_";
  protected final String TEXT_89 = " = (double)";
  protected final String TEXT_90 = ".";
  protected final String TEXT_91 = ";" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_92 = "_";
  protected final String TEXT_93 = "_";
  protected final String TEXT_94 = " = (double)0;" + NL + "}" + NL + "count_";
  protected final String TEXT_95 = "_";
  protected final String TEXT_96 = "_";
  protected final String TEXT_97 = " = 1;";
  protected final String TEXT_98 = NL + "sum_";
  protected final String TEXT_99 = "_";
  protected final String TEXT_100 = "_";
  protected final String TEXT_101 = " = (double)";
  protected final String TEXT_102 = ".";
  protected final String TEXT_103 = ";" + NL + "count_";
  protected final String TEXT_104 = "_";
  protected final String TEXT_105 = "_";
  protected final String TEXT_106 = " = 1;";
  protected final String TEXT_107 = "\t" + NL + "if(";
  protected final String TEXT_108 = ".";
  protected final String TEXT_109 = " != null){" + NL + "\tsum_";
  protected final String TEXT_110 = "_";
  protected final String TEXT_111 = "_";
  protected final String TEXT_112 = " = ";
  protected final String TEXT_113 = ".";
  protected final String TEXT_114 = ";" + NL + "\tcount_";
  protected final String TEXT_115 = "_";
  protected final String TEXT_116 = "_";
  protected final String TEXT_117 = " = 1;" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_118 = "_";
  protected final String TEXT_119 = "_";
  protected final String TEXT_120 = " = new BigDecimal(\"0.0\");" + NL + "\tcount_";
  protected final String TEXT_121 = "_";
  protected final String TEXT_122 = "_";
  protected final String TEXT_123 = " = 0;" + NL + "}";
  protected final String TEXT_124 = NL + "if(";
  protected final String TEXT_125 = ".";
  protected final String TEXT_126 = " != null){" + NL + "\tsum_";
  protected final String TEXT_127 = "_";
  protected final String TEXT_128 = "_";
  protected final String TEXT_129 = " = ";
  protected final String TEXT_130 = ".";
  protected final String TEXT_131 = ";" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_132 = "_";
  protected final String TEXT_133 = "_";
  protected final String TEXT_134 = " = new BigDecimal(\"0.0\");" + NL + "}" + NL + "count_";
  protected final String TEXT_135 = "_";
  protected final String TEXT_136 = "_";
  protected final String TEXT_137 = " = 1;";
  protected final String TEXT_138 = "set_";
  protected final String TEXT_139 = "_";
  protected final String TEXT_140 = "_";
  protected final String TEXT_141 = " = new java.util.HashSet();";
  protected final String TEXT_142 = "\tif(";
  protected final String TEXT_143 = ".";
  protected final String TEXT_144 = " != null){";
  protected final String TEXT_145 = NL + "set_";
  protected final String TEXT_146 = "_";
  protected final String TEXT_147 = "_";
  protected final String TEXT_148 = ".add(";
  protected final String TEXT_149 = ".";
  protected final String TEXT_150 = ");";
  protected final String TEXT_151 = "\t}";
  protected final String TEXT_152 = "list_object_";
  protected final String TEXT_153 = "_";
  protected final String TEXT_154 = "_";
  protected final String TEXT_155 = " = new java.util.ArrayList();";
  protected final String TEXT_156 = "\tif(";
  protected final String TEXT_157 = ".";
  protected final String TEXT_158 = " != null){";
  protected final String TEXT_159 = NL + "\t\tlist_object_";
  protected final String TEXT_160 = "_";
  protected final String TEXT_161 = "_";
  protected final String TEXT_162 = ".add(";
  protected final String TEXT_163 = ".";
  protected final String TEXT_164 = ");";
  protected final String TEXT_165 = "\t}";
  protected final String TEXT_166 = "list_";
  protected final String TEXT_167 = "_";
  protected final String TEXT_168 = "_";
  protected final String TEXT_169 = " = new StringBuilder();";
  protected final String TEXT_170 = "\tif(";
  protected final String TEXT_171 = ".";
  protected final String TEXT_172 = " != null){";
  protected final String TEXT_173 = NL + "\t\tlist_";
  protected final String TEXT_174 = "_";
  protected final String TEXT_175 = "_";
  protected final String TEXT_176 = ".append(";
  protected final String TEXT_177 = ".";
  protected final String TEXT_178 = ");";
  protected final String TEXT_179 = "\t}";
  protected final String TEXT_180 = NL + "}else{";
  protected final String TEXT_181 = NL + "while(true){";
  protected final String TEXT_182 = NL + "if(group_";
  protected final String TEXT_183 = "_";
  protected final String TEXT_184 = " != ";
  protected final String TEXT_185 = ".";
  protected final String TEXT_186 = "){" + NL + "\tsameGroup_";
  protected final String TEXT_187 = " = false;" + NL + "\tbreak;" + NL + "}";
  protected final String TEXT_188 = "if(group_";
  protected final String TEXT_189 = "_";
  protected final String TEXT_190 = " == null){" + NL + "\tif(";
  protected final String TEXT_191 = ".";
  protected final String TEXT_192 = " != null){" + NL + "\t\tsameGroup_";
  protected final String TEXT_193 = " = false;" + NL + "\t\tbreak;" + NL + "\t}" + NL + "}else{" + NL + "\tif(group_";
  protected final String TEXT_194 = "_";
  protected final String TEXT_195 = " == null || !group_";
  protected final String TEXT_196 = "_";
  protected final String TEXT_197 = ".equals(";
  protected final String TEXT_198 = ".";
  protected final String TEXT_199 = ")){" + NL + "\t\tsameGroup_";
  protected final String TEXT_200 = " = false;" + NL + "\t\tbreak;" + NL + "\t}" + NL + "}";
  protected final String TEXT_201 = "break;";
  protected final String TEXT_202 = "}" + NL + "if(sameGroup_";
  protected final String TEXT_203 = "){" + NL;
  protected final String TEXT_204 = NL + "if(";
  protected final String TEXT_205 = "_";
  protected final String TEXT_206 = "_";
  protected final String TEXT_207 = "_";
  protected final String TEXT_208 = " == null){" + NL + "\t";
  protected final String TEXT_209 = "_";
  protected final String TEXT_210 = "_";
  protected final String TEXT_211 = "_";
  protected final String TEXT_212 = " = ";
  protected final String TEXT_213 = ".";
  protected final String TEXT_214 = ";" + NL + "}";
  protected final String TEXT_215 = NL + "if(";
  protected final String TEXT_216 = ".";
  protected final String TEXT_217 = " != null){";
  protected final String TEXT_218 = NL + "\t";
  protected final String TEXT_219 = "_";
  protected final String TEXT_220 = "_";
  protected final String TEXT_221 = "_";
  protected final String TEXT_222 = " = ";
  protected final String TEXT_223 = ".";
  protected final String TEXT_224 = ";";
  protected final String TEXT_225 = NL + "}";
  protected final String TEXT_226 = NL + "\tif(";
  protected final String TEXT_227 = ".";
  protected final String TEXT_228 = " !=null){" + NL + "\t\tif(";
  protected final String TEXT_229 = "_";
  protected final String TEXT_230 = "_";
  protected final String TEXT_231 = "_";
  protected final String TEXT_232 = " == null || ";
  protected final String TEXT_233 = "_";
  protected final String TEXT_234 = "_";
  protected final String TEXT_235 = "_";
  protected final String TEXT_236 = ".compareTo(";
  protected final String TEXT_237 = ".";
  protected final String TEXT_238 = ") > 0){" + NL + "\t\t\t";
  protected final String TEXT_239 = "_";
  protected final String TEXT_240 = "_";
  protected final String TEXT_241 = "_";
  protected final String TEXT_242 = " = ";
  protected final String TEXT_243 = ".";
  protected final String TEXT_244 = ";" + NL + "\t\t}" + NL + "\t}";
  protected final String TEXT_245 = NL + "if(";
  protected final String TEXT_246 = ".";
  protected final String TEXT_247 = " !=null){" + NL + "\tif(";
  protected final String TEXT_248 = "_";
  protected final String TEXT_249 = "_";
  protected final String TEXT_250 = "_";
  protected final String TEXT_251 = " == null || ";
  protected final String TEXT_252 = "_";
  protected final String TEXT_253 = "_";
  protected final String TEXT_254 = "_";
  protected final String TEXT_255 = ".compareTo(";
  protected final String TEXT_256 = ".";
  protected final String TEXT_257 = ") > 0){" + NL + "\t\t";
  protected final String TEXT_258 = "_";
  protected final String TEXT_259 = "_";
  protected final String TEXT_260 = "_";
  protected final String TEXT_261 = " = ";
  protected final String TEXT_262 = ".";
  protected final String TEXT_263 = ";" + NL + "\t}" + NL + "}";
  protected final String TEXT_264 = NL + "if(!";
  protected final String TEXT_265 = ".";
  protected final String TEXT_266 = " && ";
  protected final String TEXT_267 = "_";
  protected final String TEXT_268 = "_";
  protected final String TEXT_269 = "_";
  protected final String TEXT_270 = "){" + NL + "\t";
  protected final String TEXT_271 = "_";
  protected final String TEXT_272 = "_";
  protected final String TEXT_273 = "_";
  protected final String TEXT_274 = " = false;" + NL + "}";
  protected final String TEXT_275 = NL + "if(";
  protected final String TEXT_276 = ".";
  protected final String TEXT_277 = " !=null){" + NL + "\tif(";
  protected final String TEXT_278 = "_";
  protected final String TEXT_279 = "_";
  protected final String TEXT_280 = "_";
  protected final String TEXT_281 = " == null || ";
  protected final String TEXT_282 = "_";
  protected final String TEXT_283 = "_";
  protected final String TEXT_284 = "_";
  protected final String TEXT_285 = ".compareTo(";
  protected final String TEXT_286 = ".";
  protected final String TEXT_287 = ") > 0){" + NL + "\t\t";
  protected final String TEXT_288 = "_";
  protected final String TEXT_289 = "_";
  protected final String TEXT_290 = "_";
  protected final String TEXT_291 = " = ";
  protected final String TEXT_292 = ".";
  protected final String TEXT_293 = ";" + NL + "\t}" + NL + "}";
  protected final String TEXT_294 = NL + "if(";
  protected final String TEXT_295 = "_";
  protected final String TEXT_296 = "_";
  protected final String TEXT_297 = "_";
  protected final String TEXT_298 = " > ";
  protected final String TEXT_299 = ".";
  protected final String TEXT_300 = "){" + NL + "\t";
  protected final String TEXT_301 = "_";
  protected final String TEXT_302 = "_";
  protected final String TEXT_303 = "_";
  protected final String TEXT_304 = " = ";
  protected final String TEXT_305 = ".";
  protected final String TEXT_306 = ";" + NL + "}";
  protected final String TEXT_307 = "if(";
  protected final String TEXT_308 = ".";
  protected final String TEXT_309 = " !=null){" + NL + "\tif(";
  protected final String TEXT_310 = "_";
  protected final String TEXT_311 = "_";
  protected final String TEXT_312 = "_";
  protected final String TEXT_313 = " == null || ";
  protected final String TEXT_314 = "_";
  protected final String TEXT_315 = "_";
  protected final String TEXT_316 = "_";
  protected final String TEXT_317 = ".compareTo(";
  protected final String TEXT_318 = ".";
  protected final String TEXT_319 = ") < 0){" + NL + "\t\t";
  protected final String TEXT_320 = "_";
  protected final String TEXT_321 = "_";
  protected final String TEXT_322 = "_";
  protected final String TEXT_323 = " = ";
  protected final String TEXT_324 = ".";
  protected final String TEXT_325 = ";" + NL + "\t}" + NL + "}";
  protected final String TEXT_326 = NL + "if(";
  protected final String TEXT_327 = ".";
  protected final String TEXT_328 = " !=null){" + NL + "\tif(";
  protected final String TEXT_329 = "_";
  protected final String TEXT_330 = "_";
  protected final String TEXT_331 = "_";
  protected final String TEXT_332 = " == null || ";
  protected final String TEXT_333 = "_";
  protected final String TEXT_334 = "_";
  protected final String TEXT_335 = "_";
  protected final String TEXT_336 = ".compareTo(";
  protected final String TEXT_337 = ".";
  protected final String TEXT_338 = ") < 0){" + NL + "\t\t";
  protected final String TEXT_339 = "_";
  protected final String TEXT_340 = "_";
  protected final String TEXT_341 = "_";
  protected final String TEXT_342 = " = ";
  protected final String TEXT_343 = ".";
  protected final String TEXT_344 = ";" + NL + "\t}" + NL + "}";
  protected final String TEXT_345 = NL + "if(";
  protected final String TEXT_346 = ".";
  protected final String TEXT_347 = " && !";
  protected final String TEXT_348 = "_";
  protected final String TEXT_349 = "_";
  protected final String TEXT_350 = "_";
  protected final String TEXT_351 = "){" + NL + "\t";
  protected final String TEXT_352 = "_";
  protected final String TEXT_353 = "_";
  protected final String TEXT_354 = "_";
  protected final String TEXT_355 = " = true;" + NL + "}";
  protected final String TEXT_356 = NL + "if(";
  protected final String TEXT_357 = ".";
  protected final String TEXT_358 = " !=null){" + NL + "\tif(";
  protected final String TEXT_359 = "_";
  protected final String TEXT_360 = "_";
  protected final String TEXT_361 = "_";
  protected final String TEXT_362 = " == null || ";
  protected final String TEXT_363 = "_";
  protected final String TEXT_364 = "_";
  protected final String TEXT_365 = "_";
  protected final String TEXT_366 = ".compareTo(";
  protected final String TEXT_367 = ".";
  protected final String TEXT_368 = ") < 0){" + NL + "\t\t";
  protected final String TEXT_369 = "_";
  protected final String TEXT_370 = "_";
  protected final String TEXT_371 = "_";
  protected final String TEXT_372 = " = ";
  protected final String TEXT_373 = ".";
  protected final String TEXT_374 = ";" + NL + "\t}" + NL + "}";
  protected final String TEXT_375 = NL + "if(";
  protected final String TEXT_376 = "_";
  protected final String TEXT_377 = "_";
  protected final String TEXT_378 = "_";
  protected final String TEXT_379 = " < ";
  protected final String TEXT_380 = ".";
  protected final String TEXT_381 = "){" + NL + "\t";
  protected final String TEXT_382 = "_";
  protected final String TEXT_383 = "_";
  protected final String TEXT_384 = "_";
  protected final String TEXT_385 = " = ";
  protected final String TEXT_386 = ".";
  protected final String TEXT_387 = ";" + NL + "}";
  protected final String TEXT_388 = "\tif(";
  protected final String TEXT_389 = ".";
  protected final String TEXT_390 = " != null){";
  protected final String TEXT_391 = "count_";
  protected final String TEXT_392 = "_";
  protected final String TEXT_393 = "_";
  protected final String TEXT_394 = " ++;";
  protected final String TEXT_395 = "\t}";
  protected final String TEXT_396 = "\tif(";
  protected final String TEXT_397 = ".";
  protected final String TEXT_398 = " != null){";
  protected final String TEXT_399 = "sum_";
  protected final String TEXT_400 = "_";
  protected final String TEXT_401 = "_";
  protected final String TEXT_402 = " += ";
  protected final String TEXT_403 = ".";
  protected final String TEXT_404 = ";";
  protected final String TEXT_405 = "\t}";
  protected final String TEXT_406 = "if(";
  protected final String TEXT_407 = ".";
  protected final String TEXT_408 = " != null){" + NL + "\tif(sum_";
  protected final String TEXT_409 = "_";
  protected final String TEXT_410 = "_";
  protected final String TEXT_411 = " == null){" + NL + "\t\tsum_";
  protected final String TEXT_412 = "_";
  protected final String TEXT_413 = "_";
  protected final String TEXT_414 = " = ";
  protected final String TEXT_415 = ".";
  protected final String TEXT_416 = ";" + NL + "\t}else{" + NL + "\t\tsum_";
  protected final String TEXT_417 = "_";
  protected final String TEXT_418 = "_";
  protected final String TEXT_419 = " = sum_";
  protected final String TEXT_420 = "_";
  protected final String TEXT_421 = "_";
  protected final String TEXT_422 = ".add(";
  protected final String TEXT_423 = ".";
  protected final String TEXT_424 = ");" + NL + "\t}" + NL + "}";
  protected final String TEXT_425 = "\tif(";
  protected final String TEXT_426 = ".";
  protected final String TEXT_427 = " != null){";
  protected final String TEXT_428 = "\tif(";
  protected final String TEXT_429 = ".";
  protected final String TEXT_430 = " != null){";
  protected final String TEXT_431 = "sum_";
  protected final String TEXT_432 = "_";
  protected final String TEXT_433 = "_";
  protected final String TEXT_434 = " += ";
  protected final String TEXT_435 = ".";
  protected final String TEXT_436 = ";";
  protected final String TEXT_437 = "\t}";
  protected final String TEXT_438 = NL + "count_";
  protected final String TEXT_439 = "_";
  protected final String TEXT_440 = "_";
  protected final String TEXT_441 = "++;";
  protected final String TEXT_442 = "\t}";
  protected final String TEXT_443 = "if(";
  protected final String TEXT_444 = ".";
  protected final String TEXT_445 = " != null){" + NL + "\tif(sum_";
  protected final String TEXT_446 = "_";
  protected final String TEXT_447 = "_";
  protected final String TEXT_448 = " == null){" + NL + "\t\tsum_";
  protected final String TEXT_449 = "_";
  protected final String TEXT_450 = "_";
  protected final String TEXT_451 = " = ";
  protected final String TEXT_452 = ".";
  protected final String TEXT_453 = ";" + NL + "\t}else{" + NL + "\t\tsum_";
  protected final String TEXT_454 = "_";
  protected final String TEXT_455 = "_";
  protected final String TEXT_456 = " = sum_";
  protected final String TEXT_457 = "_";
  protected final String TEXT_458 = "_";
  protected final String TEXT_459 = ".add(";
  protected final String TEXT_460 = ".";
  protected final String TEXT_461 = ");" + NL + "\t}";
  protected final String TEXT_462 = "count_";
  protected final String TEXT_463 = "_";
  protected final String TEXT_464 = "_";
  protected final String TEXT_465 = "++;";
  protected final String TEXT_466 = NL + "}";
  protected final String TEXT_467 = "count_";
  protected final String TEXT_468 = "_";
  protected final String TEXT_469 = "_";
  protected final String TEXT_470 = "++;";
  protected final String TEXT_471 = "\tif(";
  protected final String TEXT_472 = ".";
  protected final String TEXT_473 = " != null){";
  protected final String TEXT_474 = "_";
  protected final String TEXT_475 = "_";
  protected final String TEXT_476 = "_";
  protected final String TEXT_477 = ".append(\",\");";
  protected final String TEXT_478 = NL;
  protected final String TEXT_479 = "_";
  protected final String TEXT_480 = "_";
  protected final String TEXT_481 = "_";
  protected final String TEXT_482 = ".append(";
  protected final String TEXT_483 = ".";
  protected final String TEXT_484 = ");";
  protected final String TEXT_485 = "\t}";
  protected final String TEXT_486 = "\tif(";
  protected final String TEXT_487 = ".";
  protected final String TEXT_488 = " != null){";
  protected final String TEXT_489 = "_";
  protected final String TEXT_490 = "_";
  protected final String TEXT_491 = "_";
  protected final String TEXT_492 = ".add(";
  protected final String TEXT_493 = ".";
  protected final String TEXT_494 = ");";
  protected final String TEXT_495 = "\t}";
  protected final String TEXT_496 = "\tif(";
  protected final String TEXT_497 = ".";
  protected final String TEXT_498 = " != null){";
  protected final String TEXT_499 = NL + "set_";
  protected final String TEXT_500 = "_";
  protected final String TEXT_501 = "_";
  protected final String TEXT_502 = ".add(";
  protected final String TEXT_503 = ".";
  protected final String TEXT_504 = ");";
  protected final String TEXT_505 = "\t}";
  protected final String TEXT_506 = "}//if_same_group" + NL;
  protected final String TEXT_507 = NL + "}" + NL + "" + NL + "" + NL + "int tempCount_";
  protected final String TEXT_508 = " = -1;";
  protected final String TEXT_509 = NL + "if( !sameGroup_";
  protected final String TEXT_510 = " ){" + NL + "\ttempCount_";
  protected final String TEXT_511 = "++;";
  protected final String TEXT_512 = "emmitArray_";
  protected final String TEXT_513 = "[tempCount_";
  protected final String TEXT_514 = "].";
  protected final String TEXT_515 = " = group_";
  protected final String TEXT_516 = "_";
  protected final String TEXT_517 = ";";
  protected final String TEXT_518 = "String temp_";
  protected final String TEXT_519 = " = \"\";";
  protected final String TEXT_520 = "temp_";
  protected final String TEXT_521 = " = new String(group_";
  protected final String TEXT_522 = "_";
  protected final String TEXT_523 = ");";
  protected final String TEXT_524 = "temp_";
  protected final String TEXT_525 = " = \"\"+group_";
  protected final String TEXT_526 = "_";
  protected final String TEXT_527 = ";";
  protected final String TEXT_528 = "if(temp_";
  protected final String TEXT_529 = ".length() > 0) {";
  protected final String TEXT_530 = "emmitArray_";
  protected final String TEXT_531 = "[tempCount_";
  protected final String TEXT_532 = "].";
  protected final String TEXT_533 = " = temp_";
  protected final String TEXT_534 = ";";
  protected final String TEXT_535 = "emmitArray_";
  protected final String TEXT_536 = "[tempCount_";
  protected final String TEXT_537 = "].";
  protected final String TEXT_538 = " = ParserUtils.parseTo_Date(temp_";
  protected final String TEXT_539 = ", ";
  protected final String TEXT_540 = ");";
  protected final String TEXT_541 = "emmitArray_";
  protected final String TEXT_542 = "[tempCount_";
  protected final String TEXT_543 = "].";
  protected final String TEXT_544 = " = ParserUtils.parseTo_";
  protected final String TEXT_545 = "(temp_";
  protected final String TEXT_546 = ");";
  protected final String TEXT_547 = "} else {\t\t\t\t\t\t";
  protected final String TEXT_548 = "throw new RuntimeException(\"Value is empty for column : '";
  protected final String TEXT_549 = "' in '";
  protected final String TEXT_550 = "' connection, value is invalid or this column should be nullable or have a default value.\");";
  protected final String TEXT_551 = "emmitArray_";
  protected final String TEXT_552 = "[tempCount_";
  protected final String TEXT_553 = "].";
  protected final String TEXT_554 = " = ";
  protected final String TEXT_555 = ";";
  protected final String TEXT_556 = "}";
  protected final String TEXT_557 = "emmitArray_";
  protected final String TEXT_558 = "[tempCount_";
  protected final String TEXT_559 = "].";
  protected final String TEXT_560 = " = ";
  protected final String TEXT_561 = "_";
  protected final String TEXT_562 = "_";
  protected final String TEXT_563 = "_";
  protected final String TEXT_564 = ";";
  protected final String TEXT_565 = "emmitArray_";
  protected final String TEXT_566 = "[tempCount_";
  protected final String TEXT_567 = "].";
  protected final String TEXT_568 = " = (";
  protected final String TEXT_569 = ")";
  protected final String TEXT_570 = "_";
  protected final String TEXT_571 = "_";
  protected final String TEXT_572 = "_";
  protected final String TEXT_573 = ";";
  protected final String TEXT_574 = "String temp_";
  protected final String TEXT_575 = " = \"\";";
  protected final String TEXT_576 = "temp_";
  protected final String TEXT_577 = " = new String(";
  protected final String TEXT_578 = "_";
  protected final String TEXT_579 = "_";
  protected final String TEXT_580 = "_";
  protected final String TEXT_581 = ");";
  protected final String TEXT_582 = "temp_";
  protected final String TEXT_583 = " = \"\"+";
  protected final String TEXT_584 = "_";
  protected final String TEXT_585 = "_";
  protected final String TEXT_586 = "_";
  protected final String TEXT_587 = ";";
  protected final String TEXT_588 = "if(temp_";
  protected final String TEXT_589 = ".length() > 0) {";
  protected final String TEXT_590 = "emmitArray_";
  protected final String TEXT_591 = "[tempCount_";
  protected final String TEXT_592 = "].";
  protected final String TEXT_593 = " = temp_";
  protected final String TEXT_594 = ";";
  protected final String TEXT_595 = "emmitArray_";
  protected final String TEXT_596 = "[tempCount_";
  protected final String TEXT_597 = "].";
  protected final String TEXT_598 = " = ParserUtils.parseTo_Date(temp_";
  protected final String TEXT_599 = ", ";
  protected final String TEXT_600 = ");";
  protected final String TEXT_601 = "emmitArray_";
  protected final String TEXT_602 = "[tempCount_";
  protected final String TEXT_603 = "].";
  protected final String TEXT_604 = " = temp_";
  protected final String TEXT_605 = ".getBytes();";
  protected final String TEXT_606 = "emmitArray_";
  protected final String TEXT_607 = "[tempCount_";
  protected final String TEXT_608 = "].";
  protected final String TEXT_609 = " = ParserUtils.parseTo_";
  protected final String TEXT_610 = "(temp_";
  protected final String TEXT_611 = ");";
  protected final String TEXT_612 = "} else {\t\t\t\t\t\t";
  protected final String TEXT_613 = "throw new RuntimeException(\"Value is empty for column : '";
  protected final String TEXT_614 = "', value is invalid or this column should be nullable or have a default value.\");";
  protected final String TEXT_615 = "emmitArray_";
  protected final String TEXT_616 = "[tempCount_";
  protected final String TEXT_617 = "].";
  protected final String TEXT_618 = " = ";
  protected final String TEXT_619 = ";";
  protected final String TEXT_620 = "}";
  protected final String TEXT_621 = "emmitArray_";
  protected final String TEXT_622 = "[tempCount_";
  protected final String TEXT_623 = "].";
  protected final String TEXT_624 = " = (";
  protected final String TEXT_625 = ")";
  protected final String TEXT_626 = "_";
  protected final String TEXT_627 = "_";
  protected final String TEXT_628 = "_";
  protected final String TEXT_629 = ";";
  protected final String TEXT_630 = "emmitArray_";
  protected final String TEXT_631 = "[tempCount_";
  protected final String TEXT_632 = "].";
  protected final String TEXT_633 = " = \"\"+";
  protected final String TEXT_634 = "_";
  protected final String TEXT_635 = "_";
  protected final String TEXT_636 = "_";
  protected final String TEXT_637 = ";";
  protected final String TEXT_638 = "emmitArray_";
  protected final String TEXT_639 = "[tempCount_";
  protected final String TEXT_640 = "].";
  protected final String TEXT_641 = " = (\"\"+";
  protected final String TEXT_642 = "_";
  protected final String TEXT_643 = "_";
  protected final String TEXT_644 = "_";
  protected final String TEXT_645 = ").getBytes();";
  protected final String TEXT_646 = "emmitArray_";
  protected final String TEXT_647 = "[tempCount_";
  protected final String TEXT_648 = "].";
  protected final String TEXT_649 = " = (";
  protected final String TEXT_650 = ")";
  protected final String TEXT_651 = "_";
  protected final String TEXT_652 = "_";
  protected final String TEXT_653 = "_";
  protected final String TEXT_654 = ";";
  protected final String TEXT_655 = "emmitArray_";
  protected final String TEXT_656 = "[tempCount_";
  protected final String TEXT_657 = "].";
  protected final String TEXT_658 = " = ((";
  protected final String TEXT_659 = "))0;" + NL + "if(sum_";
  protected final String TEXT_660 = "_";
  protected final String TEXT_661 = "_";
  protected final String TEXT_662 = " != null){" + NL + "\temmitArray_";
  protected final String TEXT_663 = "[tempCount_";
  protected final String TEXT_664 = "].";
  protected final String TEXT_665 = " = ((";
  protected final String TEXT_666 = "))sum_";
  protected final String TEXT_667 = "_";
  protected final String TEXT_668 = "_";
  protected final String TEXT_669 = ".doubleValue();" + NL + "}";
  protected final String TEXT_670 = "emmitArray_";
  protected final String TEXT_671 = "[tempCount_";
  protected final String TEXT_672 = "].";
  protected final String TEXT_673 = " = BigDecimal.valueOf(sum_";
  protected final String TEXT_674 = "_";
  protected final String TEXT_675 = "_";
  protected final String TEXT_676 = ");";
  protected final String TEXT_677 = "emmitArray_";
  protected final String TEXT_678 = "[tempCount_";
  protected final String TEXT_679 = "].";
  protected final String TEXT_680 = " = sum_";
  protected final String TEXT_681 = "_";
  protected final String TEXT_682 = "_";
  protected final String TEXT_683 = ";";
  protected final String TEXT_684 = "emmitArray_";
  protected final String TEXT_685 = "[tempCount_";
  protected final String TEXT_686 = "].";
  protected final String TEXT_687 = " = String.valueOf(sum_";
  protected final String TEXT_688 = "_";
  protected final String TEXT_689 = "_";
  protected final String TEXT_690 = ");";
  protected final String TEXT_691 = "emmitArray_";
  protected final String TEXT_692 = "[tempCount_";
  protected final String TEXT_693 = "].";
  protected final String TEXT_694 = " = String.valueOf(sum_";
  protected final String TEXT_695 = "_";
  protected final String TEXT_696 = "_";
  protected final String TEXT_697 = ");";
  protected final String TEXT_698 = "double avg_";
  protected final String TEXT_699 = "_";
  protected final String TEXT_700 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_701 = "_";
  protected final String TEXT_702 = "_";
  protected final String TEXT_703 = " > 0){" + NL + "\tavg_";
  protected final String TEXT_704 = "_";
  protected final String TEXT_705 = " = sum_";
  protected final String TEXT_706 = "_";
  protected final String TEXT_707 = "_";
  protected final String TEXT_708 = "/count_";
  protected final String TEXT_709 = "_";
  protected final String TEXT_710 = "_";
  protected final String TEXT_711 = ";" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_712 = "[tempCount_";
  protected final String TEXT_713 = "].";
  protected final String TEXT_714 = " = (";
  protected final String TEXT_715 = ")avg_";
  protected final String TEXT_716 = "_";
  protected final String TEXT_717 = ";";
  protected final String TEXT_718 = "double avg_";
  protected final String TEXT_719 = "_";
  protected final String TEXT_720 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_721 = "_";
  protected final String TEXT_722 = "_";
  protected final String TEXT_723 = " > 0 && sum_";
  protected final String TEXT_724 = "_";
  protected final String TEXT_725 = "_";
  protected final String TEXT_726 = " != null){" + NL + "\tavg_";
  protected final String TEXT_727 = "_";
  protected final String TEXT_728 = " = sum_";
  protected final String TEXT_729 = "_";
  protected final String TEXT_730 = "_";
  protected final String TEXT_731 = ".divide(BigDecimal.valueOf(count_";
  protected final String TEXT_732 = "_";
  protected final String TEXT_733 = "_";
  protected final String TEXT_734 = "), ";
  protected final String TEXT_735 = ", BigDecimal.ROUND_HALF_UP).doubleValue();" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_736 = "[tempCount_";
  protected final String TEXT_737 = "].";
  protected final String TEXT_738 = " = (";
  protected final String TEXT_739 = ")avg_";
  protected final String TEXT_740 = "_";
  protected final String TEXT_741 = ";";
  protected final String TEXT_742 = "BigDecimal avg_";
  protected final String TEXT_743 = "_";
  protected final String TEXT_744 = " = new BigDecimal(\"0.0\");" + NL + "if(count_";
  protected final String TEXT_745 = "_";
  protected final String TEXT_746 = "_";
  protected final String TEXT_747 = " > 0){" + NL + "\tavg_";
  protected final String TEXT_748 = "_";
  protected final String TEXT_749 = " = BigDecimal.valueOf(sum_";
  protected final String TEXT_750 = "_";
  protected final String TEXT_751 = "_";
  protected final String TEXT_752 = ").divide(BigDecimal.valueOf(count_";
  protected final String TEXT_753 = "_";
  protected final String TEXT_754 = "_";
  protected final String TEXT_755 = "), ";
  protected final String TEXT_756 = ", BigDecimal.ROUND_HALF_UP);" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_757 = "[tempCount_";
  protected final String TEXT_758 = "].";
  protected final String TEXT_759 = " = avg_";
  protected final String TEXT_760 = "_";
  protected final String TEXT_761 = ";";
  protected final String TEXT_762 = "BigDecimal avg_";
  protected final String TEXT_763 = "_";
  protected final String TEXT_764 = " = new BigDecimal(\"0.0\");" + NL + "if(count_";
  protected final String TEXT_765 = "_";
  protected final String TEXT_766 = "_";
  protected final String TEXT_767 = " > 0 && sum_";
  protected final String TEXT_768 = "_";
  protected final String TEXT_769 = "_";
  protected final String TEXT_770 = " != null){" + NL + "\tavg_";
  protected final String TEXT_771 = "_";
  protected final String TEXT_772 = " = sum_";
  protected final String TEXT_773 = "_";
  protected final String TEXT_774 = "_";
  protected final String TEXT_775 = ".divide(BigDecimal.valueOf(count_";
  protected final String TEXT_776 = "_";
  protected final String TEXT_777 = "_";
  protected final String TEXT_778 = "), ";
  protected final String TEXT_779 = ", BigDecimal.ROUND_HALF_UP);" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_780 = "[tempCount_";
  protected final String TEXT_781 = "].";
  protected final String TEXT_782 = " = avg_";
  protected final String TEXT_783 = "_";
  protected final String TEXT_784 = ";";
  protected final String TEXT_785 = "double avg_";
  protected final String TEXT_786 = "_";
  protected final String TEXT_787 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_788 = "_";
  protected final String TEXT_789 = "_";
  protected final String TEXT_790 = " > 0){" + NL + "\tavg_";
  protected final String TEXT_791 = "_";
  protected final String TEXT_792 = " = sum_";
  protected final String TEXT_793 = "_";
  protected final String TEXT_794 = "_";
  protected final String TEXT_795 = "/count_";
  protected final String TEXT_796 = "_";
  protected final String TEXT_797 = "_";
  protected final String TEXT_798 = ";" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_799 = "[tempCount_";
  protected final String TEXT_800 = "].";
  protected final String TEXT_801 = " = String.valueOf(avg_";
  protected final String TEXT_802 = "_";
  protected final String TEXT_803 = ");";
  protected final String TEXT_804 = "double avg_";
  protected final String TEXT_805 = "_";
  protected final String TEXT_806 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_807 = "_";
  protected final String TEXT_808 = "_";
  protected final String TEXT_809 = " > 0 && sum_";
  protected final String TEXT_810 = "_";
  protected final String TEXT_811 = "_";
  protected final String TEXT_812 = " != null){" + NL + "\tavg_";
  protected final String TEXT_813 = "_";
  protected final String TEXT_814 = " = sum_";
  protected final String TEXT_815 = "_";
  protected final String TEXT_816 = "_";
  protected final String TEXT_817 = ".divide(BigDecimal.valueOf(count_";
  protected final String TEXT_818 = "_";
  protected final String TEXT_819 = "_";
  protected final String TEXT_820 = "), ";
  protected final String TEXT_821 = ", BigDecimal.ROUND_HALF_UP).doubleValue();" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_822 = "[tempCount_";
  protected final String TEXT_823 = "].";
  protected final String TEXT_824 = " = String.valueOf(avg_";
  protected final String TEXT_825 = "_";
  protected final String TEXT_826 = ");";
  protected final String TEXT_827 = NL + "if(true){" + NL + "\tthrow new Exception(\"In column ";
  protected final String TEXT_828 = ", the data type \\\"";
  protected final String TEXT_829 = "\\\" is not applicable for \\\"avg\\\" result.\");" + NL + "}";
  protected final String TEXT_830 = "emmitArray_";
  protected final String TEXT_831 = "[tempCount_";
  protected final String TEXT_832 = "].";
  protected final String TEXT_833 = " = (";
  protected final String TEXT_834 = ")set_";
  protected final String TEXT_835 = "_";
  protected final String TEXT_836 = "_";
  protected final String TEXT_837 = ".size();";
  protected final String TEXT_838 = "emmitArray_";
  protected final String TEXT_839 = "[tempCount_";
  protected final String TEXT_840 = "].";
  protected final String TEXT_841 = " = BigDecimal.valueOf(set_";
  protected final String TEXT_842 = "_";
  protected final String TEXT_843 = "_";
  protected final String TEXT_844 = ".size());";
  protected final String TEXT_845 = "emmitArray_";
  protected final String TEXT_846 = "[tempCount_";
  protected final String TEXT_847 = "].";
  protected final String TEXT_848 = " = \"\"+set_";
  protected final String TEXT_849 = "_";
  protected final String TEXT_850 = "_";
  protected final String TEXT_851 = ".size();";
  protected final String TEXT_852 = "emmitArray_";
  protected final String TEXT_853 = "[tempCount_";
  protected final String TEXT_854 = "].";
  protected final String TEXT_855 = " = (\"\"+set_";
  protected final String TEXT_856 = "_";
  protected final String TEXT_857 = "_";
  protected final String TEXT_858 = ".size()).getBytes();";
  protected final String TEXT_859 = "emmitArray_";
  protected final String TEXT_860 = "[tempCount_";
  protected final String TEXT_861 = "].";
  protected final String TEXT_862 = " = ";
  protected final String TEXT_863 = "_";
  protected final String TEXT_864 = "_";
  protected final String TEXT_865 = "_";
  protected final String TEXT_866 = ".toString();";
  protected final String TEXT_867 = "emmitArray_";
  protected final String TEXT_868 = "[tempCount_";
  protected final String TEXT_869 = "].";
  protected final String TEXT_870 = " = ";
  protected final String TEXT_871 = "_";
  protected final String TEXT_872 = "_";
  protected final String TEXT_873 = "_";
  protected final String TEXT_874 = ";";
  protected final String TEXT_875 = "emmitArray_";
  protected final String TEXT_876 = "[tempCount_";
  protected final String TEXT_877 = "].";
  protected final String TEXT_878 = " = ";
  protected final String TEXT_879 = "_";
  protected final String TEXT_880 = "_";
  protected final String TEXT_881 = "_";
  protected final String TEXT_882 = ";";
  protected final String TEXT_883 = "emmitArray_";
  protected final String TEXT_884 = "[tempCount_";
  protected final String TEXT_885 = "].";
  protected final String TEXT_886 = " = ";
  protected final String TEXT_887 = "_";
  protected final String TEXT_888 = "_";
  protected final String TEXT_889 = "_";
  protected final String TEXT_890 = ".toString();";
  protected final String TEXT_891 = "if(true){" + NL + "\tthrow new Exception(\"In column ";
  protected final String TEXT_892 = ", data type \\\"List\\\" is not applicable for aggregate function \\\"list\\\" result. Please try aggregate function \\\"list(object)\\\"!\");" + NL + "}";
  protected final String TEXT_893 = "if(true){" + NL + "\tthrow new Exception(\"In column ";
  protected final String TEXT_894 = ", the data type \\\"";
  protected final String TEXT_895 = "\\\" is not applicable for \\\"list\\\" result.\");" + NL + "}";
  protected final String TEXT_896 = NL + "\t\t\tgroup_";
  protected final String TEXT_897 = "_";
  protected final String TEXT_898 = " = ";
  protected final String TEXT_899 = ".";
  protected final String TEXT_900 = ".clone();";
  protected final String TEXT_901 = NL + "\t\t\tgroup_";
  protected final String TEXT_902 = "_";
  protected final String TEXT_903 = " = ";
  protected final String TEXT_904 = ".";
  protected final String TEXT_905 = ";";
  protected final String TEXT_906 = "_";
  protected final String TEXT_907 = "_";
  protected final String TEXT_908 = "_";
  protected final String TEXT_909 = " = ";
  protected final String TEXT_910 = ".";
  protected final String TEXT_911 = ";";
  protected final String TEXT_912 = "\tif(";
  protected final String TEXT_913 = ".";
  protected final String TEXT_914 = " != null){";
  protected final String TEXT_915 = "count_";
  protected final String TEXT_916 = "_";
  protected final String TEXT_917 = "_";
  protected final String TEXT_918 = " = 1;";
  protected final String TEXT_919 = "\t}else{" + NL + "count_";
  protected final String TEXT_920 = "_";
  protected final String TEXT_921 = "_";
  protected final String TEXT_922 = " = 0;" + NL + "}";
  protected final String TEXT_923 = "\tif(";
  protected final String TEXT_924 = ".";
  protected final String TEXT_925 = " != null){";
  protected final String TEXT_926 = NL + "\t\t";
  protected final String TEXT_927 = "_";
  protected final String TEXT_928 = "_";
  protected final String TEXT_929 = "_";
  protected final String TEXT_930 = " = (double)";
  protected final String TEXT_931 = ".";
  protected final String TEXT_932 = ";";
  protected final String TEXT_933 = "\t}else{" + NL + "\t\t";
  protected final String TEXT_934 = "_";
  protected final String TEXT_935 = "_";
  protected final String TEXT_936 = "_";
  protected final String TEXT_937 = " = (double)0;" + NL + "\t}";
  protected final String TEXT_938 = "\tif(";
  protected final String TEXT_939 = ".";
  protected final String TEXT_940 = " != null){";
  protected final String TEXT_941 = NL + "\t\t";
  protected final String TEXT_942 = "_";
  protected final String TEXT_943 = "_";
  protected final String TEXT_944 = "_";
  protected final String TEXT_945 = " = ";
  protected final String TEXT_946 = ".";
  protected final String TEXT_947 = ";";
  protected final String TEXT_948 = "\t}else{" + NL + "\t\t";
  protected final String TEXT_949 = "_";
  protected final String TEXT_950 = "_";
  protected final String TEXT_951 = "_";
  protected final String TEXT_952 = " = new BigDecimal(\"0.0\");" + NL + "\t}";
  protected final String TEXT_953 = NL + "if(";
  protected final String TEXT_954 = ".";
  protected final String TEXT_955 = " != null){" + NL + "\tsum_";
  protected final String TEXT_956 = "_";
  protected final String TEXT_957 = "_";
  protected final String TEXT_958 = " = (double)";
  protected final String TEXT_959 = ".";
  protected final String TEXT_960 = ";" + NL + "\tcount_";
  protected final String TEXT_961 = "_";
  protected final String TEXT_962 = "_";
  protected final String TEXT_963 = " = 1;" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_964 = "_";
  protected final String TEXT_965 = "_";
  protected final String TEXT_966 = " = (double)0;" + NL + "\tcount_";
  protected final String TEXT_967 = "_";
  protected final String TEXT_968 = "_";
  protected final String TEXT_969 = " = 0;" + NL + "}";
  protected final String TEXT_970 = NL + "if(";
  protected final String TEXT_971 = ".";
  protected final String TEXT_972 = " != null){" + NL + "\tsum_";
  protected final String TEXT_973 = "_";
  protected final String TEXT_974 = "_";
  protected final String TEXT_975 = " = (double)";
  protected final String TEXT_976 = ".";
  protected final String TEXT_977 = ";" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_978 = "_";
  protected final String TEXT_979 = "_";
  protected final String TEXT_980 = " = (double)0;" + NL + "}" + NL + "count_";
  protected final String TEXT_981 = "_";
  protected final String TEXT_982 = "_";
  protected final String TEXT_983 = " = 1;";
  protected final String TEXT_984 = NL + "sum_";
  protected final String TEXT_985 = "_";
  protected final String TEXT_986 = "_";
  protected final String TEXT_987 = " = (double)";
  protected final String TEXT_988 = ".";
  protected final String TEXT_989 = ";" + NL + "count_";
  protected final String TEXT_990 = "_";
  protected final String TEXT_991 = "_";
  protected final String TEXT_992 = " = 1;";
  protected final String TEXT_993 = "\t" + NL + "if(";
  protected final String TEXT_994 = ".";
  protected final String TEXT_995 = " != null){" + NL + "\tsum_";
  protected final String TEXT_996 = "_";
  protected final String TEXT_997 = "_";
  protected final String TEXT_998 = " = ";
  protected final String TEXT_999 = ".";
  protected final String TEXT_1000 = ";" + NL + "\tcount_";
  protected final String TEXT_1001 = "_";
  protected final String TEXT_1002 = "_";
  protected final String TEXT_1003 = " = 1;" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_1004 = "_";
  protected final String TEXT_1005 = "_";
  protected final String TEXT_1006 = " = new BigDecimal(\"0.0\");" + NL + "\tcount_";
  protected final String TEXT_1007 = "_";
  protected final String TEXT_1008 = "_";
  protected final String TEXT_1009 = " = 0;" + NL + "}";
  protected final String TEXT_1010 = NL + "if(";
  protected final String TEXT_1011 = ".";
  protected final String TEXT_1012 = " != null){" + NL + "\tsum_";
  protected final String TEXT_1013 = "_";
  protected final String TEXT_1014 = "_";
  protected final String TEXT_1015 = " = ";
  protected final String TEXT_1016 = ".";
  protected final String TEXT_1017 = ";" + NL + "}else{" + NL + "\tsum_";
  protected final String TEXT_1018 = "_";
  protected final String TEXT_1019 = "_";
  protected final String TEXT_1020 = " = new BigDecimal(\"0.0\");" + NL + "}" + NL + "count_";
  protected final String TEXT_1021 = "_";
  protected final String TEXT_1022 = "_";
  protected final String TEXT_1023 = " = 1;";
  protected final String TEXT_1024 = "set_";
  protected final String TEXT_1025 = "_";
  protected final String TEXT_1026 = "_";
  protected final String TEXT_1027 = " = new java.util.HashSet();";
  protected final String TEXT_1028 = "\tif(";
  protected final String TEXT_1029 = ".";
  protected final String TEXT_1030 = " != null){";
  protected final String TEXT_1031 = NL + "set_";
  protected final String TEXT_1032 = "_";
  protected final String TEXT_1033 = "_";
  protected final String TEXT_1034 = ".add(";
  protected final String TEXT_1035 = ".";
  protected final String TEXT_1036 = ");";
  protected final String TEXT_1037 = "\t}";
  protected final String TEXT_1038 = "list_object_";
  protected final String TEXT_1039 = "_";
  protected final String TEXT_1040 = "_";
  protected final String TEXT_1041 = " = new java.util.ArrayList();";
  protected final String TEXT_1042 = "\tif(";
  protected final String TEXT_1043 = ".";
  protected final String TEXT_1044 = " != null){";
  protected final String TEXT_1045 = NL + "\t\tlist_object_";
  protected final String TEXT_1046 = "_";
  protected final String TEXT_1047 = "_";
  protected final String TEXT_1048 = ".add(";
  protected final String TEXT_1049 = ".";
  protected final String TEXT_1050 = ");";
  protected final String TEXT_1051 = "\t}";
  protected final String TEXT_1052 = "list_";
  protected final String TEXT_1053 = "_";
  protected final String TEXT_1054 = "_";
  protected final String TEXT_1055 = " = new StringBuilder();";
  protected final String TEXT_1056 = "\tif(";
  protected final String TEXT_1057 = ".";
  protected final String TEXT_1058 = " != null){";
  protected final String TEXT_1059 = NL + "\t\tlist_";
  protected final String TEXT_1060 = "_";
  protected final String TEXT_1061 = "_";
  protected final String TEXT_1062 = ".append(";
  protected final String TEXT_1063 = ".";
  protected final String TEXT_1064 = ");";
  protected final String TEXT_1065 = "\t}";
  protected final String TEXT_1066 = NL + "}";
  protected final String TEXT_1067 = NL + "if( currentRowIndex_";
  protected final String TEXT_1068 = "  == ";
  protected final String TEXT_1069 = " ){" + NL + "\ttempCount_";
  protected final String TEXT_1070 = "++;";
  protected final String TEXT_1071 = "emmitArray_";
  protected final String TEXT_1072 = "[tempCount_";
  protected final String TEXT_1073 = "].";
  protected final String TEXT_1074 = " = group_";
  protected final String TEXT_1075 = "_";
  protected final String TEXT_1076 = ";";
  protected final String TEXT_1077 = "String temp_";
  protected final String TEXT_1078 = " = \"\";";
  protected final String TEXT_1079 = "temp_";
  protected final String TEXT_1080 = " = new String(group_";
  protected final String TEXT_1081 = "_";
  protected final String TEXT_1082 = ");";
  protected final String TEXT_1083 = "temp_";
  protected final String TEXT_1084 = " = \"\"+group_";
  protected final String TEXT_1085 = "_";
  protected final String TEXT_1086 = ";";
  protected final String TEXT_1087 = "if(temp_";
  protected final String TEXT_1088 = ".length() > 0) {";
  protected final String TEXT_1089 = "emmitArray_";
  protected final String TEXT_1090 = "[tempCount_";
  protected final String TEXT_1091 = "].";
  protected final String TEXT_1092 = " = temp_";
  protected final String TEXT_1093 = ";";
  protected final String TEXT_1094 = "emmitArray_";
  protected final String TEXT_1095 = "[tempCount_";
  protected final String TEXT_1096 = "].";
  protected final String TEXT_1097 = " = ParserUtils.parseTo_Date(temp_";
  protected final String TEXT_1098 = ", ";
  protected final String TEXT_1099 = ");";
  protected final String TEXT_1100 = "emmitArray_";
  protected final String TEXT_1101 = "[tempCount_";
  protected final String TEXT_1102 = "].";
  protected final String TEXT_1103 = " = ParserUtils.parseTo_";
  protected final String TEXT_1104 = "(temp_";
  protected final String TEXT_1105 = ");";
  protected final String TEXT_1106 = "} else {\t\t\t\t\t\t";
  protected final String TEXT_1107 = "throw new RuntimeException(\"Value is empty for column : '";
  protected final String TEXT_1108 = "' in '";
  protected final String TEXT_1109 = "' connection, value is invalid or this column should be nullable or have a default value.\");";
  protected final String TEXT_1110 = "emmitArray_";
  protected final String TEXT_1111 = "[tempCount_";
  protected final String TEXT_1112 = "].";
  protected final String TEXT_1113 = " = ";
  protected final String TEXT_1114 = ";";
  protected final String TEXT_1115 = "}";
  protected final String TEXT_1116 = "emmitArray_";
  protected final String TEXT_1117 = "[tempCount_";
  protected final String TEXT_1118 = "].";
  protected final String TEXT_1119 = " = ";
  protected final String TEXT_1120 = "_";
  protected final String TEXT_1121 = "_";
  protected final String TEXT_1122 = "_";
  protected final String TEXT_1123 = ";";
  protected final String TEXT_1124 = "emmitArray_";
  protected final String TEXT_1125 = "[tempCount_";
  protected final String TEXT_1126 = "].";
  protected final String TEXT_1127 = " = (";
  protected final String TEXT_1128 = ")";
  protected final String TEXT_1129 = "_";
  protected final String TEXT_1130 = "_";
  protected final String TEXT_1131 = "_";
  protected final String TEXT_1132 = ";";
  protected final String TEXT_1133 = "String temp_";
  protected final String TEXT_1134 = " = \"\";";
  protected final String TEXT_1135 = "temp_";
  protected final String TEXT_1136 = " = new String(";
  protected final String TEXT_1137 = "_";
  protected final String TEXT_1138 = "_";
  protected final String TEXT_1139 = "_";
  protected final String TEXT_1140 = ");";
  protected final String TEXT_1141 = "temp_";
  protected final String TEXT_1142 = " = \"\"+";
  protected final String TEXT_1143 = "_";
  protected final String TEXT_1144 = "_";
  protected final String TEXT_1145 = "_";
  protected final String TEXT_1146 = ";";
  protected final String TEXT_1147 = "if(temp_";
  protected final String TEXT_1148 = ".length() > 0) {";
  protected final String TEXT_1149 = "emmitArray_";
  protected final String TEXT_1150 = "[tempCount_";
  protected final String TEXT_1151 = "].";
  protected final String TEXT_1152 = " = temp_";
  protected final String TEXT_1153 = ";";
  protected final String TEXT_1154 = "emmitArray_";
  protected final String TEXT_1155 = "[tempCount_";
  protected final String TEXT_1156 = "].";
  protected final String TEXT_1157 = " = ParserUtils.parseTo_Date(temp_";
  protected final String TEXT_1158 = ", ";
  protected final String TEXT_1159 = ");";
  protected final String TEXT_1160 = "emmitArray_";
  protected final String TEXT_1161 = "[tempCount_";
  protected final String TEXT_1162 = "].";
  protected final String TEXT_1163 = " = temp_";
  protected final String TEXT_1164 = ".getBytes();";
  protected final String TEXT_1165 = "emmitArray_";
  protected final String TEXT_1166 = "[tempCount_";
  protected final String TEXT_1167 = "].";
  protected final String TEXT_1168 = " = ParserUtils.parseTo_";
  protected final String TEXT_1169 = "(temp_";
  protected final String TEXT_1170 = ");";
  protected final String TEXT_1171 = "} else {\t\t\t\t\t\t";
  protected final String TEXT_1172 = "throw new RuntimeException(\"Value is empty for column : '";
  protected final String TEXT_1173 = "' in '";
  protected final String TEXT_1174 = "' connection, value is invalid or this column should be nullable or have a default value.\");";
  protected final String TEXT_1175 = "emmitArray_";
  protected final String TEXT_1176 = "[tempCount_";
  protected final String TEXT_1177 = "].";
  protected final String TEXT_1178 = " = ";
  protected final String TEXT_1179 = ";";
  protected final String TEXT_1180 = "}";
  protected final String TEXT_1181 = "emmitArray_";
  protected final String TEXT_1182 = "[tempCount_";
  protected final String TEXT_1183 = "].";
  protected final String TEXT_1184 = " = (";
  protected final String TEXT_1185 = ")";
  protected final String TEXT_1186 = "_";
  protected final String TEXT_1187 = "_";
  protected final String TEXT_1188 = "_";
  protected final String TEXT_1189 = ";";
  protected final String TEXT_1190 = "emmitArray_";
  protected final String TEXT_1191 = "[tempCount_";
  protected final String TEXT_1192 = "].";
  protected final String TEXT_1193 = " = \"\"+";
  protected final String TEXT_1194 = "_";
  protected final String TEXT_1195 = "_";
  protected final String TEXT_1196 = "_";
  protected final String TEXT_1197 = ";";
  protected final String TEXT_1198 = "emmitArray_";
  protected final String TEXT_1199 = "[tempCount_";
  protected final String TEXT_1200 = "].";
  protected final String TEXT_1201 = " = (\"\"+";
  protected final String TEXT_1202 = "_";
  protected final String TEXT_1203 = "_";
  protected final String TEXT_1204 = "_";
  protected final String TEXT_1205 = ").getBytes();";
  protected final String TEXT_1206 = "emmitArray_";
  protected final String TEXT_1207 = "[tempCount_";
  protected final String TEXT_1208 = "].";
  protected final String TEXT_1209 = " = (";
  protected final String TEXT_1210 = ")";
  protected final String TEXT_1211 = "_";
  protected final String TEXT_1212 = "_";
  protected final String TEXT_1213 = "_";
  protected final String TEXT_1214 = ";";
  protected final String TEXT_1215 = "emmitArray_";
  protected final String TEXT_1216 = "[tempCount_";
  protected final String TEXT_1217 = "].";
  protected final String TEXT_1218 = " = ((";
  protected final String TEXT_1219 = "))0;" + NL + "if(sum_";
  protected final String TEXT_1220 = "_";
  protected final String TEXT_1221 = "_";
  protected final String TEXT_1222 = " != null){" + NL + "\temmitArray_";
  protected final String TEXT_1223 = "[tempCount_";
  protected final String TEXT_1224 = "].";
  protected final String TEXT_1225 = " = ((";
  protected final String TEXT_1226 = "))sum_";
  protected final String TEXT_1227 = "_";
  protected final String TEXT_1228 = "_";
  protected final String TEXT_1229 = ".doubleValue();" + NL + "}";
  protected final String TEXT_1230 = "emmitArray_";
  protected final String TEXT_1231 = "[tempCount_";
  protected final String TEXT_1232 = "].";
  protected final String TEXT_1233 = " = BigDecimal.valueOf(sum_";
  protected final String TEXT_1234 = "_";
  protected final String TEXT_1235 = "_";
  protected final String TEXT_1236 = ");";
  protected final String TEXT_1237 = "emmitArray_";
  protected final String TEXT_1238 = "[tempCount_";
  protected final String TEXT_1239 = "].";
  protected final String TEXT_1240 = " = sum_";
  protected final String TEXT_1241 = "_";
  protected final String TEXT_1242 = "_";
  protected final String TEXT_1243 = ";";
  protected final String TEXT_1244 = "emmitArray_";
  protected final String TEXT_1245 = "[tempCount_";
  protected final String TEXT_1246 = "].";
  protected final String TEXT_1247 = " = String.valueOf(sum_";
  protected final String TEXT_1248 = "_";
  protected final String TEXT_1249 = "_";
  protected final String TEXT_1250 = ");";
  protected final String TEXT_1251 = "emmitArray_";
  protected final String TEXT_1252 = "[tempCount_";
  protected final String TEXT_1253 = "].";
  protected final String TEXT_1254 = " = String.valueOf(sum_";
  protected final String TEXT_1255 = "_";
  protected final String TEXT_1256 = "_";
  protected final String TEXT_1257 = ");";
  protected final String TEXT_1258 = "double avg_";
  protected final String TEXT_1259 = "_";
  protected final String TEXT_1260 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_1261 = "_";
  protected final String TEXT_1262 = "_";
  protected final String TEXT_1263 = " > 0){" + NL + "\tavg_";
  protected final String TEXT_1264 = "_";
  protected final String TEXT_1265 = " = sum_";
  protected final String TEXT_1266 = "_";
  protected final String TEXT_1267 = "_";
  protected final String TEXT_1268 = "/count_";
  protected final String TEXT_1269 = "_";
  protected final String TEXT_1270 = "_";
  protected final String TEXT_1271 = ";" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_1272 = "[tempCount_";
  protected final String TEXT_1273 = "].";
  protected final String TEXT_1274 = " = (";
  protected final String TEXT_1275 = ")avg_";
  protected final String TEXT_1276 = "_";
  protected final String TEXT_1277 = ";";
  protected final String TEXT_1278 = "double avg_";
  protected final String TEXT_1279 = "_";
  protected final String TEXT_1280 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_1281 = "_";
  protected final String TEXT_1282 = "_";
  protected final String TEXT_1283 = " > 0 && sum_";
  protected final String TEXT_1284 = "_";
  protected final String TEXT_1285 = "_";
  protected final String TEXT_1286 = " != null){" + NL + "\tavg_";
  protected final String TEXT_1287 = "_";
  protected final String TEXT_1288 = " = sum_";
  protected final String TEXT_1289 = "_";
  protected final String TEXT_1290 = "_";
  protected final String TEXT_1291 = ".divide(BigDecimal.valueOf(count_";
  protected final String TEXT_1292 = "_";
  protected final String TEXT_1293 = "_";
  protected final String TEXT_1294 = "), ";
  protected final String TEXT_1295 = ", BigDecimal.ROUND_HALF_UP).doubleValue();" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_1296 = "[tempCount_";
  protected final String TEXT_1297 = "].";
  protected final String TEXT_1298 = " = (";
  protected final String TEXT_1299 = ")avg_";
  protected final String TEXT_1300 = "_";
  protected final String TEXT_1301 = ";";
  protected final String TEXT_1302 = "BigDecimal avg_";
  protected final String TEXT_1303 = "_";
  protected final String TEXT_1304 = " = new BigDecimal(\"0.0\");" + NL + "if(count_";
  protected final String TEXT_1305 = "_";
  protected final String TEXT_1306 = "_";
  protected final String TEXT_1307 = " > 0){" + NL + "\tavg_";
  protected final String TEXT_1308 = "_";
  protected final String TEXT_1309 = " = BigDecimal.valueOf(sum_";
  protected final String TEXT_1310 = "_";
  protected final String TEXT_1311 = "_";
  protected final String TEXT_1312 = ").divide(BigDecimal.valueOf(count_";
  protected final String TEXT_1313 = "_";
  protected final String TEXT_1314 = "_";
  protected final String TEXT_1315 = "), ";
  protected final String TEXT_1316 = ", BigDecimal.ROUND_HALF_UP);" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_1317 = "[tempCount_";
  protected final String TEXT_1318 = "].";
  protected final String TEXT_1319 = " = avg_";
  protected final String TEXT_1320 = "_";
  protected final String TEXT_1321 = ";";
  protected final String TEXT_1322 = "BigDecimal avg_";
  protected final String TEXT_1323 = "_";
  protected final String TEXT_1324 = " = new BigDecimal(\"0.0\");" + NL + "if(count_";
  protected final String TEXT_1325 = "_";
  protected final String TEXT_1326 = "_";
  protected final String TEXT_1327 = " > 0 && sum_";
  protected final String TEXT_1328 = "_";
  protected final String TEXT_1329 = "_";
  protected final String TEXT_1330 = " != null){" + NL + "\tavg_";
  protected final String TEXT_1331 = "_";
  protected final String TEXT_1332 = " = sum_";
  protected final String TEXT_1333 = "_";
  protected final String TEXT_1334 = "_";
  protected final String TEXT_1335 = ".divide(BigDecimal.valueOf(count_";
  protected final String TEXT_1336 = "_";
  protected final String TEXT_1337 = "_";
  protected final String TEXT_1338 = "), ";
  protected final String TEXT_1339 = ", BigDecimal.ROUND_HALF_UP);" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_1340 = "[tempCount_";
  protected final String TEXT_1341 = "].";
  protected final String TEXT_1342 = " = avg_";
  protected final String TEXT_1343 = "_";
  protected final String TEXT_1344 = ";";
  protected final String TEXT_1345 = "double avg_";
  protected final String TEXT_1346 = "_";
  protected final String TEXT_1347 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_1348 = "_";
  protected final String TEXT_1349 = "_";
  protected final String TEXT_1350 = " > 0){" + NL + "\tavg_";
  protected final String TEXT_1351 = "_";
  protected final String TEXT_1352 = " = sum_";
  protected final String TEXT_1353 = "_";
  protected final String TEXT_1354 = "_";
  protected final String TEXT_1355 = "/count_";
  protected final String TEXT_1356 = "_";
  protected final String TEXT_1357 = "_";
  protected final String TEXT_1358 = ";" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_1359 = "[tempCount_";
  protected final String TEXT_1360 = "].";
  protected final String TEXT_1361 = " = String.valueOf(avg_";
  protected final String TEXT_1362 = "_";
  protected final String TEXT_1363 = ");";
  protected final String TEXT_1364 = "double avg_";
  protected final String TEXT_1365 = "_";
  protected final String TEXT_1366 = " = 0d;" + NL + "if(count_";
  protected final String TEXT_1367 = "_";
  protected final String TEXT_1368 = "_";
  protected final String TEXT_1369 = " > 0 && sum_";
  protected final String TEXT_1370 = "_";
  protected final String TEXT_1371 = "_";
  protected final String TEXT_1372 = " != null){" + NL + "\tavg_";
  protected final String TEXT_1373 = "_";
  protected final String TEXT_1374 = " = sum_";
  protected final String TEXT_1375 = "_";
  protected final String TEXT_1376 = "_";
  protected final String TEXT_1377 = ".divide(BigDecimal.valueOf(count_";
  protected final String TEXT_1378 = "_";
  protected final String TEXT_1379 = "_";
  protected final String TEXT_1380 = "), ";
  protected final String TEXT_1381 = ", BigDecimal.ROUND_HALF_UP).doubleValue();" + NL + "}" + NL + "emmitArray_";
  protected final String TEXT_1382 = "[tempCount_";
  protected final String TEXT_1383 = "].";
  protected final String TEXT_1384 = " = String.valueOf(avg_";
  protected final String TEXT_1385 = "_";
  protected final String TEXT_1386 = ");";
  protected final String TEXT_1387 = "if(true){" + NL + "\tthrow new Exception(\"In column ";
  protected final String TEXT_1388 = ", the data type \\\"";
  protected final String TEXT_1389 = "\\\" is not applicable for \\\"avg\\\" result.\");" + NL + "}";
  protected final String TEXT_1390 = "emmitArray_";
  protected final String TEXT_1391 = "[tempCount_";
  protected final String TEXT_1392 = "].";
  protected final String TEXT_1393 = " = (";
  protected final String TEXT_1394 = ")set_";
  protected final String TEXT_1395 = "_";
  protected final String TEXT_1396 = "_";
  protected final String TEXT_1397 = ".size();";
  protected final String TEXT_1398 = "emmitArray_";
  protected final String TEXT_1399 = "[tempCount_";
  protected final String TEXT_1400 = "].";
  protected final String TEXT_1401 = " = BigDecimal.valueOf(set_";
  protected final String TEXT_1402 = "_";
  protected final String TEXT_1403 = "_";
  protected final String TEXT_1404 = ".size());";
  protected final String TEXT_1405 = "emmitArray_";
  protected final String TEXT_1406 = "[tempCount_";
  protected final String TEXT_1407 = "].";
  protected final String TEXT_1408 = " = \"\"+set_";
  protected final String TEXT_1409 = "_";
  protected final String TEXT_1410 = "_";
  protected final String TEXT_1411 = ".size();";
  protected final String TEXT_1412 = "emmitArray_";
  protected final String TEXT_1413 = "[tempCount_";
  protected final String TEXT_1414 = "].";
  protected final String TEXT_1415 = " = (\"\"+set_";
  protected final String TEXT_1416 = "_";
  protected final String TEXT_1417 = "_";
  protected final String TEXT_1418 = ".size()).getBytes();";
  protected final String TEXT_1419 = "emmitArray_";
  protected final String TEXT_1420 = "[tempCount_";
  protected final String TEXT_1421 = "].";
  protected final String TEXT_1422 = " = ";
  protected final String TEXT_1423 = "_";
  protected final String TEXT_1424 = "_";
  protected final String TEXT_1425 = "_";
  protected final String TEXT_1426 = ".toString();";
  protected final String TEXT_1427 = "emmitArray_";
  protected final String TEXT_1428 = "[tempCount_";
  protected final String TEXT_1429 = "].";
  protected final String TEXT_1430 = " = ";
  protected final String TEXT_1431 = "_";
  protected final String TEXT_1432 = "_";
  protected final String TEXT_1433 = "_";
  protected final String TEXT_1434 = ";";
  protected final String TEXT_1435 = "emmitArray_";
  protected final String TEXT_1436 = "[tempCount_";
  protected final String TEXT_1437 = "].";
  protected final String TEXT_1438 = " = ";
  protected final String TEXT_1439 = "_";
  protected final String TEXT_1440 = "_";
  protected final String TEXT_1441 = "_";
  protected final String TEXT_1442 = ";";
  protected final String TEXT_1443 = "emmitArray_";
  protected final String TEXT_1444 = "[tempCount_";
  protected final String TEXT_1445 = "].";
  protected final String TEXT_1446 = " = ";
  protected final String TEXT_1447 = "_";
  protected final String TEXT_1448 = "_";
  protected final String TEXT_1449 = "_";
  protected final String TEXT_1450 = ".toString();";
  protected final String TEXT_1451 = "if(true){" + NL + "\tthrow new Exception(\"In column ";
  protected final String TEXT_1452 = ", data type \\\"List\\\" is not applicable for aggregate function \\\"list\\\" result. Please try aggregate function \\\"list(object)\\\"!\");" + NL + "}";
  protected final String TEXT_1453 = "if(true){" + NL + "\tthrow new Exception(\"In column ";
  protected final String TEXT_1454 = ", the data type \\\"";
  protected final String TEXT_1455 = "\\\" is not applicable for \\\"list\\\" result.\");" + NL + "}";
  protected final String TEXT_1456 = NL + "}" + NL + "for(int i_";
  protected final String TEXT_1457 = "=0; i_";
  protected final String TEXT_1458 = " <= tempCount_";
  protected final String TEXT_1459 = "; i_";
  protected final String TEXT_1460 = "++){";
  protected final String TEXT_1461 = ".";
  protected final String TEXT_1462 = " = emmitArray_";
  protected final String TEXT_1463 = "[i_";
  protected final String TEXT_1464 = "].";
  protected final String TEXT_1465 = ";    \t\t\t\t";
  protected final String TEXT_1466 = "nb_line_";
  protected final String TEXT_1467 = "++;";

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    List< ? extends IConnection> conns = node.getIncomingConnections();
    IMetadataTable inMetadata = null;
    String connName = "";
    if(conns != null){
    for (IConnection conn : conns) { 
		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) { 
			inMetadata = conn.getMetadataTable();
			connName = conn.getName();
    		break;
		}
	}
    if (metadata != null && inMetadata != null) { 
		List<IMetadataColumn> columns = metadata.getListColumns();
    	List<IMetadataColumn> inColumns = inMetadata.getListColumns();
		List<Map<String, String>> operations = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__OPERATIONS__");
		IMetadataColumn[][] column_op = new IMetadataColumn[operations.size()][2];
		String[] functions = new String[operations.size()];
		boolean[] needTestForNull = new boolean[operations.size()];
		List<Map<String, String>> groupbys = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__GROUPBYS__");
		String rowCount = ElementParameterParser.getValue(node,"__ROW_COUNT__");
		IMetadataColumn[][] column_gr = new IMetadataColumn[groupbys.size()][2];
	    for(int i = 0; i < column_gr.length; i++){
			Map<String, String> groupby = groupbys.get(i);
			String in = groupby.get("INPUT_COLUMN");
			String out = groupby.get("OUTPUT_COLUMN");
			for (IMetadataColumn column: inColumns) {
				if(column.getLabel().equals(in)){
					column_gr[i][0] = column;
					break;
				}
			}
    		for (IMetadataColumn column: columns) {
				if(column.getLabel().equals(out)){
					column_gr[i][1] = column;
					break;
				}
			}
		}
		for(int i = 0; i < column_op.length; i++){
			Map<String, String> operation = operations.get(i);
			String in = operation.get("INPUT_COLUMN");
			String out = operation.get("OUTPUT_COLUMN");
			functions[i] = operation.get("FUNCTION");
			for (IMetadataColumn column: inColumns) {
				if(column.getLabel().equals(in)){
					column_op[i][0] = column;
					JavaType inputJavaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					needTestForNull[i] = !(JavaTypesManager.isJavaPrimitiveType(inputJavaType, column.isNullable())) && (operation.get("IGNORE_NULL").equals("true"));
					break;
				}
			}
    		for (IMetadataColumn column: columns) {
				if(column.getLabel().equals(out)){
					column_op[i][1] = column;
					break;
				}
			}
		}

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_3);
    
if(column_gr.length > 0){

    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    
}

    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_8);
    
	for(int i = 0; i < column_gr.length; i++){
		if("id_Dynamic".equals(column_gr[i][0].getTalendType())) {

    stringBuffer.append(TEXT_9);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_13);
    
		} else {

    stringBuffer.append(TEXT_14);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_18);
    	
		}
	}
		
	for(int i = 0; i < column_op.length; i++){//__HSS_tAR_001
		boolean duplicate = false;
		for(int j = 0; j < i; j++){
			if(functions[j].equals(functions[i]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
				duplicate = true;
				break;
			}
		}
		if(duplicate){
			continue;
		}
		
		JavaType javaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
		if(("min").equals(functions[i]) || ("max").equals(functions[i]) || ("first").equals(functions[i]) || ("last").equals(functions[i])){ 

    stringBuffer.append(TEXT_19);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_21);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_22);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_24);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_25);
    
		}else if(("count").equals(functions[i])){
			boolean countHasAvg = false;
			for(int j = 0; j < functions.length; j++){
				if(("avg").equals(functions[j]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
					countHasAvg = true;
					break;
				}
			}
			if(!countHasAvg){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_26);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_27);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_28);
    
				}

    stringBuffer.append(TEXT_29);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_32);
    			
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_33);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_34);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_36);
    
				}
			}
		}else if(("sum").equals(functions[i])){
			boolean sumHasAvg = false;
			for(int j = 0; j < functions.length; j++){
				if(("avg").equals(functions[j]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
					sumHasAvg = true;
					break;
				}
			}
			if(!sumHasAvg){
				if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
					|| javaType == JavaTypesManager.DOUBLE ){
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_37);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_39);
    
					}

    stringBuffer.append(TEXT_40);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_46);
    					
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_47);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_51);
    
					}
				}else if(javaType == JavaTypesManager.BIGDECIMAL){
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_52);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_54);
    
					}

    stringBuffer.append(TEXT_55);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_61);
    				
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_62);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    
					}
				}else{
//generate nothing.
				}
			}
		}else if(("avg").equals(functions[i])){
			if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
					|| javaType == JavaTypesManager.DOUBLE ){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_67);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_73);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_74);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_81);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_83);
    
				}else{
					if(column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_84);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_91);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_92);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_93);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_96);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_97);
    
					}else{

    stringBuffer.append(TEXT_98);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_101);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_102);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    
					}
				}
			}else if(javaType == JavaTypesManager.BIGDECIMAL){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_107);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_109);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_115);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_117);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_120);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_123);
    
				}else{

    stringBuffer.append(TEXT_124);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_125);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_126);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_127);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_128);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_129);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_130);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_131);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_132);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_134);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_135);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_136);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_137);
    
				}
			}else{
//generate nothing.
			}
		}else if(("distinct").equals(functions[i])){

    stringBuffer.append(TEXT_138);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_139);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_140);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_141);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_142);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_143);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_144);
    
			}

    stringBuffer.append(TEXT_145);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_146);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_148);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_149);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_150);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_151);
    
			}
		}else if(("list_object").equals(functions[i])){

    stringBuffer.append(TEXT_152);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_153);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_155);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_156);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_157);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_158);
    
			}

    stringBuffer.append(TEXT_159);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_160);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_161);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_162);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_163);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_164);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_165);
    
			}
		}else  if(("list").equals(functions[i])){

    stringBuffer.append(TEXT_166);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_167);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_168);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_169);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_170);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_171);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_172);
    
			}

    stringBuffer.append(TEXT_173);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_174);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_175);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_176);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_177);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_178);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_179);
    
			}
		}
	}//__HSS_tAR_001

    stringBuffer.append(TEXT_180);
    
if(column_gr.length > 0){//while loop

    stringBuffer.append(TEXT_181);
    	for(int i = 0; i < column_gr.length; i++){
		JavaType javaType = JavaTypesManager.getJavaTypeFromId(column_gr[i][0].getTalendType());
		if(JavaTypesManager.isNumberType(javaType) && javaType != JavaTypesManager.BIGDECIMAL && !column_gr[i][0].isNullable()){

    stringBuffer.append(TEXT_182);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_183);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_184);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_185);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_186);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_187);
    
		} else {

    stringBuffer.append(TEXT_188);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_189);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_190);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_191);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_192);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_193);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_194);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_195);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_196);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_197);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_198);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_200);
    		}
		
		if(i+1 == column_gr.length){

    stringBuffer.append(TEXT_201);
    		}
	}

    stringBuffer.append(TEXT_202);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_203);
    
}//while loop end
	
    
	for(int i = 0; i < column_op.length; i++){//__HSS_tAR_002
		boolean duplicate = false;
		for(int j = 0; j < i; j++){
			if(functions[j].equals(functions[i]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
				duplicate = true;
				break;
			}
		}
		if(duplicate){
			continue;
		}
		JavaType javaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
		if(("first").equals(functions[i])){
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_204);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_205);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_206);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_208);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_209);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_210);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_211);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_212);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_213);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_214);
    
			}
		}else if(("last").equals(functions[i])){
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_215);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_216);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_217);
    
			}

    stringBuffer.append(TEXT_218);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_219);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_220);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_221);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_222);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_223);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_224);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_225);
    
			}
		}else if(("min").equals(functions[i])){
			if(javaType == JavaTypesManager.LIST || javaType == JavaTypesManager.OBJECT || javaType == JavaTypesManager.BYTE_ARRAY){
			//do nothing
			}else if(javaType == JavaTypesManager.BIGDECIMAL || javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.DATE){

    stringBuffer.append(TEXT_226);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_227);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_228);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_229);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_230);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_232);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_233);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_234);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_236);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_237);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_238);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_239);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_240);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_241);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_242);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_243);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_244);
    			}else if(javaType == JavaTypesManager.BOOLEAN){
				if(needTestForNull[i] || column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_245);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_246);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_247);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_248);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_249);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_250);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_251);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_252);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_253);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_254);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_255);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_256);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_257);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_258);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_259);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_260);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_261);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_262);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_263);
    
				}else{

    stringBuffer.append(TEXT_264);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_265);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_266);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_267);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_268);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_269);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_270);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_271);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_272);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_273);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_274);
    
				}
			}else{
				if(needTestForNull[i] || column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_275);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_276);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_277);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_278);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_279);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_280);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_281);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_282);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_283);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_284);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_285);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_286);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_287);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_288);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_289);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_290);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_291);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_292);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_293);
    
				}else{

    stringBuffer.append(TEXT_294);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_295);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_296);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_297);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_298);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_299);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_300);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_301);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_302);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_303);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_304);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_305);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_306);
    
				}
			}
		}else if(("max").equals(functions[i])){
			if(javaType == JavaTypesManager.LIST || javaType == JavaTypesManager.OBJECT || javaType == JavaTypesManager.BYTE_ARRAY){
			//do nothing
			}else if(javaType == JavaTypesManager.BIGDECIMAL || javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.DATE){

    stringBuffer.append(TEXT_307);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_308);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_309);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_310);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_311);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_312);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_313);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_314);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_315);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_316);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_317);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_318);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_319);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_320);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_321);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_322);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_323);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_324);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_325);
    			}else if(javaType == JavaTypesManager.BOOLEAN){
				if(needTestForNull[i] || column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_326);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_327);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_328);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_329);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_330);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_332);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_333);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_334);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_335);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_336);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_337);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_338);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_339);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_340);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_341);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_342);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_343);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_344);
    
				}else{

    stringBuffer.append(TEXT_345);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_346);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_347);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_348);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_349);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_350);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_351);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_352);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_353);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_354);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_355);
    
				}
			}else{
				if(needTestForNull[i] || column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_356);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_357);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_358);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_359);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_360);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_361);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_362);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_363);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_364);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_365);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_366);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_367);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_368);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_369);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_370);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_371);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_372);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_373);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_374);
    
				}else{

    stringBuffer.append(TEXT_375);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_376);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_377);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_378);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_379);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_380);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_381);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_382);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_383);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_384);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_385);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_386);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_387);
    
				}
			}
		}else if(("count").equals(functions[i])){
			boolean countHasAvg = false;
			for(int j = 0; j < functions.length; j++){
				if(("avg").equals(functions[j]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
					countHasAvg = true;
					break;
				}
			}
			if(!countHasAvg){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_388);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_389);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_390);
    
				}

    stringBuffer.append(TEXT_391);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_392);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_393);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_394);
    			
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_395);
    
				}
			}
		}else if(("sum").equals(functions[i])){
			boolean sumHasAvg = false;
			for(int j = 0; j < functions.length; j++){
				if(("avg").equals(functions[j]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
					sumHasAvg = true;
					break;
				}
			}
			if(!sumHasAvg){
				if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
					|| javaType == JavaTypesManager.DOUBLE){
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_396);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_397);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_398);
    
					}

    stringBuffer.append(TEXT_399);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_400);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_401);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_402);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_403);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_404);
    
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_405);
    
					}
				}else if(javaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_406);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_407);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_408);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_409);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_410);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_411);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_412);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_413);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_414);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_415);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_416);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_417);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_418);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_419);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_420);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_421);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_422);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_423);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_424);
    
				}
			}
		}else if(("avg").equals(functions[i])){
			if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
					|| javaType == JavaTypesManager.DOUBLE){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_425);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_426);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_427);
    
				}
				if(!needTestForNull[i] && column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_428);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_429);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_430);
    
				}

    stringBuffer.append(TEXT_431);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_432);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_433);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_434);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_435);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_436);
    				if(!needTestForNull[i] && column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_437);
    
				}

    stringBuffer.append(TEXT_438);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_439);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_440);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_441);
    
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_442);
    
				}
			}else if(javaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_443);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_444);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_445);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_446);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_447);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_448);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_449);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_450);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_451);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_452);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_453);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_454);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_455);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_456);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_457);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_458);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_459);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_460);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_461);
    
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_462);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_463);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_464);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_465);
    
				}

    stringBuffer.append(TEXT_466);
    
				if(!needTestForNull[i]){

    stringBuffer.append(TEXT_467);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_468);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_469);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_470);
    
				}
			}
		}else if(("list").equals(functions[i])){
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_471);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_472);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_473);
    
			}

    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_474);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_475);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_476);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_477);
    stringBuffer.append(TEXT_478);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_479);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_480);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_481);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_482);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_483);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_484);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_485);
    
			}
		}else if(("list_object").equals(functions[i])){
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_486);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_487);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_488);
    
			}

    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_489);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_490);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_491);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_492);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_493);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_494);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_495);
    
			}
		}else if(("distinct").equals(functions[i])){
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_496);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_497);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_498);
    
			}

    stringBuffer.append(TEXT_499);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_500);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_501);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_502);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_503);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_504);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_505);
    
			}
		}
	}//__HSS_tAR_002

if(column_gr.length > 0){

    stringBuffer.append(TEXT_506);
    
}

    stringBuffer.append(TEXT_507);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_508);
    



if(column_gr.length > 0){
	//??

    stringBuffer.append(TEXT_509);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_510);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_511);
    
	//do out start ...
	conns = null;
	conns = node.getOutgoingSortedConnections();
	if (conns!=null) {
		if (conns.size()>0) {
			IConnection conn = conns.get(0);
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) { 
				List<IMetadataColumn> listColumns = metadata.getListColumns();
				int sizeListColumns = listColumns.size();
				boolean flag = true;
				next_column:
				for (int valueN=0; valueN < sizeListColumns; valueN++) {
					IMetadataColumn column = listColumns.get(valueN);
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
					for(int i = 0; i < column_gr.length; i++){
						if(column.getLabel().equals(column_gr[i][1].getLabel())){
							JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_gr[i][0].getTalendType());
							if(inJavaType == javaType){

    stringBuffer.append(TEXT_512);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_513);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_514);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_515);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_516);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_517);
    							}else{//for different data type
								if(flag){
									flag = false;

    stringBuffer.append(TEXT_518);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_519);
    								}
								if(inJavaType == JavaTypesManager.BYTE_ARRAY){

    stringBuffer.append(TEXT_520);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_521);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_522);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_523);
    								}else{

    stringBuffer.append(TEXT_524);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_525);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_526);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_527);
    								}

    stringBuffer.append(TEXT_528);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_529);
    								if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_530);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_531);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_532);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_533);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_534);
    								} else if(javaType == JavaTypesManager.DATE) { 

    stringBuffer.append(TEXT_535);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_536);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_537);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_538);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_539);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_540);
    								} else {

    stringBuffer.append(TEXT_541);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_542);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_543);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_544);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_545);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_546);
    								}

    stringBuffer.append(TEXT_547);
    								String defaultValue = JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate, column.getDefault());
								if(defaultValue == null) {

    stringBuffer.append(TEXT_548);
    stringBuffer.append( column.getLabel() );
    stringBuffer.append(TEXT_549);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_550);
    								} else {

    stringBuffer.append(TEXT_551);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_552);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_553);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_554);
    stringBuffer.append(defaultValue );
    stringBuffer.append(TEXT_555);
    								}

    stringBuffer.append(TEXT_556);
    							}							
							continue next_column;
						}
					}					
					for(int i = 0; i < column_op.length; i++){
						if(column.getLabel().equals(column_op[i][1].getLabel())){
							if(("min").equals(functions[i]) || ("max").equals(functions[i]) || ("first").equals(functions[i]) || ("last").equals(functions[i])){
								JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
								if(inJavaType == javaType){

    stringBuffer.append(TEXT_557);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_558);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_559);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_560);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_561);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_562);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_563);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_564);
    								}else{//for different data type
									if((javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
										|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
										|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
										|| javaType == JavaTypesManager.DOUBLE)&&(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE)){

    stringBuffer.append(TEXT_565);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_566);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_567);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_568);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_569);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_570);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_571);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_572);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_573);
    									}else{
										if(flag){
											flag = false;

    stringBuffer.append(TEXT_574);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_575);
    										}
										if(inJavaType == JavaTypesManager.BYTE_ARRAY){

    stringBuffer.append(TEXT_576);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_577);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_578);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_579);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_580);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_581);
    										}else{

    stringBuffer.append(TEXT_582);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_583);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_584);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_585);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_586);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_587);
    										}

    stringBuffer.append(TEXT_588);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_589);
    										if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_590);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_591);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_592);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_593);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_594);
    										} else if(javaType == JavaTypesManager.DATE) { 

    stringBuffer.append(TEXT_595);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_596);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_597);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_598);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_599);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_600);
    										} else if(javaType == JavaTypesManager.BYTE_ARRAY) { 

    stringBuffer.append(TEXT_601);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_602);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_603);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_604);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_605);
    										} else {

    stringBuffer.append(TEXT_606);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_607);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_608);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_609);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_610);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_611);
    										}

    stringBuffer.append(TEXT_612);
    										String defaultValue = JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate, column.getDefault());
										if(defaultValue == null) {

    stringBuffer.append(TEXT_613);
    stringBuffer.append( column.getLabel() );
    stringBuffer.append(TEXT_614);
    										} else {

    stringBuffer.append(TEXT_615);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_616);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_617);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_618);
    stringBuffer.append(defaultValue );
    stringBuffer.append(TEXT_619);
    										}

    stringBuffer.append(TEXT_620);
    									}
								}
							}else if(("count").equals(functions[i])){
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_621);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_622);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_623);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_624);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_625);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_626);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_627);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_628);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_629);
    								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_630);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_631);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_632);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_633);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_634);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_635);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_636);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_637);
    								}else if(javaType == JavaTypesManager.BYTE_ARRAY) {

    stringBuffer.append(TEXT_638);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_639);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_640);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_641);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_642);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_643);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_644);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_645);
    								}
							}else if(("sum").equals(functions[i])){
								JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE ){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_646);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_647);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_648);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_649);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_650);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_651);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_652);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_653);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_654);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_655);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_656);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_657);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_658);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_659);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_660);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_661);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_662);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_663);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_664);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_665);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_666);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_667);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_668);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_669);
    
									}
								}else if(javaType == JavaTypesManager.BIGDECIMAL) {
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_670);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_671);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_672);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_673);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_674);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_675);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_676);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_677);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_678);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_679);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_680);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_681);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_682);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_683);
    
									}
								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_684);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_685);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_686);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_687);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_688);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_689);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_690);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_691);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_692);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_693);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_694);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_695);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_696);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_697);
    
									}
								}else{
//generate nothing
								}
							}else if(("avg").equals(functions[i])){
								JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_698);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_699);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_700);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_701);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_702);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_703);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_704);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_705);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_706);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_707);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_708);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_709);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_710);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_711);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_712);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_713);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_714);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_715);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_716);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_717);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){
										int pre = 10;
                       		 			Integer precision = column.getPrecision();
                           		 		if(precision!=null && precision!=0) { 
                           		 			pre = precision;
                           		 		}

    stringBuffer.append(TEXT_718);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_719);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_720);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_721);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_722);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_723);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_724);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_725);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_726);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_727);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_728);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_729);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_730);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_731);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_732);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_733);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_734);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_735);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_736);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_737);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_738);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_739);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_740);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_741);
    
									}

    							}else if(javaType == JavaTypesManager.BIGDECIMAL) {
									int pre = 10;
                       		 		Integer precision = column.getPrecision();
                           		 	if(precision!=null && precision!=0) { 
                           		 		pre = precision;
                           		 	}
                           		 	if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_742);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_743);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_744);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_745);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_746);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_747);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_748);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_749);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_750);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_751);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_752);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_753);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_754);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_755);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_756);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_757);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_758);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_759);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_760);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_761);
    
                           		 	}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_762);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_763);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_764);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_765);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_766);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_767);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_768);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_769);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_770);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_771);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_772);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_773);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_774);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_775);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_776);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_777);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_778);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_779);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_780);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_781);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_782);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_783);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_784);
    
                           		 	}
								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_785);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_786);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_787);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_788);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_789);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_790);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_791);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_792);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_793);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_794);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_795);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_796);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_797);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_798);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_799);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_800);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_801);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_802);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_803);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){
										int pre = 10;
                       		 			Integer precision = column.getPrecision();
                           		 		if(precision!=null && precision!=0) { 
                           		 			pre = precision;
                           		 		}

    stringBuffer.append(TEXT_804);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_805);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_806);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_807);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_808);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_809);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_810);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_811);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_812);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_813);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_814);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_815);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_816);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_817);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_818);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_819);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_820);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_821);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_822);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_823);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_824);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_825);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_826);
    
									}
								}else{

    stringBuffer.append(TEXT_827);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_828);
    stringBuffer.append(JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable()) );
    stringBuffer.append(TEXT_829);
    								}
							}else if(("distinct").equals(functions[i])){
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_830);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_831);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_832);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_833);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_834);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_835);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_836);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_837);
    								}else if(javaType == JavaTypesManager.BIGDECIMAL) {

    stringBuffer.append(TEXT_838);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_839);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_840);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_841);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_842);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_843);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_844);
    								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_845);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_846);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_847);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_848);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_849);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_850);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_851);
    								}else if(javaType == JavaTypesManager.BYTE_ARRAY) {

    stringBuffer.append(TEXT_852);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_853);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_854);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_855);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_856);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_857);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_858);
    								}
							}else if(("list_object").equals(functions[i])){
								if(javaType == JavaTypesManager.STRING){

    stringBuffer.append(TEXT_859);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_860);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_861);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_862);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_863);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_864);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_865);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_866);
    
								}else if(javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_867);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_868);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_869);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_870);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_871);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_872);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_873);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_874);
    								}else if(javaType == JavaTypesManager.LIST) {

    stringBuffer.append(TEXT_875);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_876);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_877);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_878);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_879);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_880);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_881);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_882);
    								}
							}else if(("list").equals(functions[i])){
								if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_883);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_884);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_885);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_886);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_887);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_888);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_889);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_890);
    								} else if(javaType == JavaTypesManager.LIST){

    stringBuffer.append(TEXT_891);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_892);
    
								}else{

    stringBuffer.append(TEXT_893);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_894);
    stringBuffer.append(JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable()) );
    stringBuffer.append(TEXT_895);
    								}
							}
							continue next_column;
						}
					}
				}
			}
		}
	}
	//do out end ...
	
	//do first
	for(int i = 0; i < column_gr.length; i++){
		if("id_Dynamic".equals(column_gr[i][0].getTalendType())) {

    stringBuffer.append(TEXT_896);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_897);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_898);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_899);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_900);
    
		} else {

    stringBuffer.append(TEXT_901);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_902);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_903);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_904);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_905);
    	
		}
	}
	for(int i = 0; i < column_op.length && column_gr.length > 0; i++){
		boolean duplicate = false;
		for(int j = 0; j < i; j++){
			if(functions[j].equals(functions[i]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
				duplicate = true;
				break;
			}
		}
		if(duplicate){
			continue;
		}
		JavaType javaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
		if(("min").equals(functions[i]) || ("max").equals(functions[i]) || ("first").equals(functions[i]) || ("last").equals(functions[i])){ 

    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_906);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_907);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_908);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_909);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_910);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_911);
    		}else if(("count").equals(functions[i])){
			boolean countHasAvg = false;
			for(int j = 0; j < functions.length; j++){
				if(("avg").equals(functions[j]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
					countHasAvg = true;
					break;
				}
			}
			if(!countHasAvg){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_912);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_913);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_914);
    
				}

    stringBuffer.append(TEXT_915);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_916);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_917);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_918);
    			
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_919);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_920);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_921);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_922);
    
				}
			}
		}else if(("sum").equals(functions[i])){
			boolean sumHasAvg = false;
			for(int j = 0; j < functions.length; j++){
				if(("avg").equals(functions[j]) && column_op[j][0].getLabel().equals(column_op[i][0].getLabel()) && needTestForNull[i] == needTestForNull[j]){
					sumHasAvg = true;
					break;
				}
			}
			if(!sumHasAvg){
				if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
					|| javaType == JavaTypesManager.DOUBLE ){
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_923);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_924);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_925);
    
					}

    stringBuffer.append(TEXT_926);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_927);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_928);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_929);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_930);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_931);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_932);
    					
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_933);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_934);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_935);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_936);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_937);
    
					}
				}else if(javaType == JavaTypesManager.BIGDECIMAL){
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_938);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_939);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_940);
    
					}

    stringBuffer.append(TEXT_941);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_942);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_943);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_944);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_945);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_946);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_947);
    				
					if(needTestForNull[i]){

    stringBuffer.append(TEXT_948);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_949);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_950);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_951);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_952);
    
					}
				}else{
//generate nothing.
				}
			}
		}else if(("avg").equals(functions[i])){
			if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
					|| javaType == JavaTypesManager.DOUBLE ){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_953);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_954);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_955);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_956);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_957);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_958);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_959);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_960);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_961);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_962);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_963);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_964);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_965);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_966);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_967);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_968);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_969);
    
				}else{
					if(column_op[i][0].isNullable()){

    stringBuffer.append(TEXT_970);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_971);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_972);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_973);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_974);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_975);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_976);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_977);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_978);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_979);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_980);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_981);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_982);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_983);
    
					}else{

    stringBuffer.append(TEXT_984);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_985);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_986);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_987);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_988);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_989);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_990);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_991);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_992);
    
					}
				}
			}else if(javaType == JavaTypesManager.BIGDECIMAL){
				if(needTestForNull[i]){

    stringBuffer.append(TEXT_993);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_994);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_995);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_996);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_997);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_998);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_999);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1000);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1001);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1002);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1003);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1004);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1005);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1006);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1007);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1008);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1009);
    
				}else{

    stringBuffer.append(TEXT_1010);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1011);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1012);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1013);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1014);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1015);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1016);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1017);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1018);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1019);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1020);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1021);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1022);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1023);
    
				}
			}else{
//generate nothing.
			}
		}else if(("distinct").equals(functions[i])){

    stringBuffer.append(TEXT_1024);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1025);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1026);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1027);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_1028);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1029);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1030);
    
			}

    stringBuffer.append(TEXT_1031);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1032);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1033);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1034);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1035);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1036);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_1037);
    
			}
		}else if(("list_object").equals(functions[i])){

    stringBuffer.append(TEXT_1038);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1039);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1040);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1041);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_1042);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1043);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1044);
    
			}

    stringBuffer.append(TEXT_1045);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1046);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1047);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1048);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1049);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1050);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_1051);
    
			}
		}else  if(("list").equals(functions[i])){

    stringBuffer.append(TEXT_1052);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1053);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1054);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1055);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_1056);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1057);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1058);
    
			}

    stringBuffer.append(TEXT_1059);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1060);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1061);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1062);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_1063);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1064);
    
			if(needTestForNull[i]){

    stringBuffer.append(TEXT_1065);
    
			}
		}
	}

    stringBuffer.append(TEXT_1066);
    
	//??
}
/////////////////////////////////////////////////
	//??

    stringBuffer.append(TEXT_1067);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_1068);
    stringBuffer.append(rowCount );
    stringBuffer.append(TEXT_1069);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1070);
    
	//do out start ...
	conns = null;
	conns = node.getOutgoingSortedConnections();
	if (conns!=null) {
		if (conns.size()>0) {
			IConnection conn = conns.get(0);
			if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) { 
				List<IMetadataColumn> listColumns = metadata.getListColumns();
				int sizeListColumns = listColumns.size();
				boolean flag = true;
				next_column:
				for (int valueN=0; valueN < sizeListColumns; valueN++) {
					IMetadataColumn column = listColumns.get(valueN);
					String typeToGenerate = JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable());
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					String patternValue = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
					for(int i = 0; i < column_gr.length; i++){
						if(column.getLabel().equals(column_gr[i][1].getLabel())){
							JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_gr[i][0].getTalendType());
							if(inJavaType == javaType){

    stringBuffer.append(TEXT_1071);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1072);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1073);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1074);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_1075);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1076);
    							}else{//for different data type
								if(flag){
									flag = false;

    stringBuffer.append(TEXT_1077);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1078);
    								}
								if(inJavaType == JavaTypesManager.BYTE_ARRAY){

    stringBuffer.append(TEXT_1079);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1080);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_1081);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1082);
    								}else{

    stringBuffer.append(TEXT_1083);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1084);
    stringBuffer.append(column_gr[i][0].getLabel() );
    stringBuffer.append(TEXT_1085);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1086);
    								}

    stringBuffer.append(TEXT_1087);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1088);
    								if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_1089);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1090);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1091);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1092);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1093);
    								} else if(javaType == JavaTypesManager.DATE) { 

    stringBuffer.append(TEXT_1094);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1095);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1096);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1097);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1098);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_1099);
    								} else {

    stringBuffer.append(TEXT_1100);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1102);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1103);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_1104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1105);
    								}

    stringBuffer.append(TEXT_1106);
    								String defaultValue = JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate, column.getDefault());
								if(defaultValue == null) {

    stringBuffer.append(TEXT_1107);
    stringBuffer.append( column.getLabel() );
    stringBuffer.append(TEXT_1108);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_1109);
    								} else {

    stringBuffer.append(TEXT_1110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1112);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1113);
    stringBuffer.append(defaultValue );
    stringBuffer.append(TEXT_1114);
    								}

    stringBuffer.append(TEXT_1115);
    							}							
							continue next_column;
						}
					}					
					for(int i = 0; i < column_op.length; i++){
						if(column.getLabel().equals(column_op[i][1].getLabel())){
							if(("min").equals(functions[i]) || ("max").equals(functions[i]) || ("first").equals(functions[i]) || ("last").equals(functions[i])){ 
								JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
								if(inJavaType == javaType){

    stringBuffer.append(TEXT_1116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1118);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1119);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1120);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1121);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1123);
    								}else{//for different data type
									if((javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
										|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
										|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
										|| javaType == JavaTypesManager.DOUBLE)&&(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE)){

    stringBuffer.append(TEXT_1124);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1125);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1126);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1127);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1128);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1129);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1130);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1131);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1132);
    									}else{
										if(flag){
											flag = false;

    stringBuffer.append(TEXT_1133);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1134);
    										}
										if(inJavaType == JavaTypesManager.BYTE_ARRAY){

    stringBuffer.append(TEXT_1135);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1136);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1137);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1138);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1139);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1140);
    										}else{

    stringBuffer.append(TEXT_1141);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1142);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1143);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1144);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1145);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1146);
    										}

    stringBuffer.append(TEXT_1147);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1148);
    										if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_1149);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1150);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1151);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1152);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1153);
    										} else if(javaType == JavaTypesManager.DATE) { 

    stringBuffer.append(TEXT_1154);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1155);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1156);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1157);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1158);
    stringBuffer.append( patternValue );
    stringBuffer.append(TEXT_1159);
    										} else if(javaType == JavaTypesManager.BYTE_ARRAY) { 

    stringBuffer.append(TEXT_1160);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1161);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1162);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1163);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1164);
    										} else {

    stringBuffer.append(TEXT_1165);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1166);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1167);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1168);
    stringBuffer.append( typeToGenerate );
    stringBuffer.append(TEXT_1169);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1170);
    										}

    stringBuffer.append(TEXT_1171);
    										String defaultValue = JavaTypesManager.getDefaultValueFromJavaType(typeToGenerate, column.getDefault());
										if(defaultValue == null) {

    stringBuffer.append(TEXT_1172);
    stringBuffer.append( column.getLabel() );
    stringBuffer.append(TEXT_1173);
    stringBuffer.append(conn.getName());
    stringBuffer.append(TEXT_1174);
    										} else {

    stringBuffer.append(TEXT_1175);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1176);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1177);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1178);
    stringBuffer.append(defaultValue );
    stringBuffer.append(TEXT_1179);
    										}

    stringBuffer.append(TEXT_1180);
    									}
								}
							}else if(("count").equals(functions[i])){
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1181);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1182);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1183);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1184);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1185);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1186);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1187);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1188);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1189);
    								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_1190);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1191);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1192);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1193);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1194);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1195);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1196);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1197);
    								}else if(javaType == JavaTypesManager.BYTE_ARRAY) {

    stringBuffer.append(TEXT_1198);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1199);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1200);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1201);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1202);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1203);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1204);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1205);
    								}
							}else if(("sum").equals(functions[i])){
								JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE ){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1206);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1207);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1208);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1209);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1210);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1211);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1212);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1213);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1214);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_1215);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1216);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1217);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1218);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1219);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1220);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1221);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1222);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1223);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1224);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1225);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1226);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1227);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1228);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1229);
    
									}
								}else if(javaType == JavaTypesManager.BIGDECIMAL) {
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1230);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1231);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1232);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1233);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1234);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1235);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1236);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_1237);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1238);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1239);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1240);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1241);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1242);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1243);
    
									}
								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1244);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1245);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1246);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1247);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1248);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1249);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1250);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_1251);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1252);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1253);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1254);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1255);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1256);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1257);
    
									}
								}else{
//generate nothing...
								}
							}else if(("avg").equals(functions[i])){
								JavaType inJavaType = JavaTypesManager.getJavaTypeFromId(column_op[i][0].getTalendType());
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1258);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1259);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1260);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1261);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1262);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1263);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1264);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1265);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1266);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1267);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1268);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1269);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1270);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1271);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1272);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1273);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1274);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1275);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1276);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1277);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){
										int pre = 10;
                       		 			Integer precision = column.getPrecision();
                           		 		if(precision!=null && precision!=0) { 
                           		 			pre = precision;
                           		 		}

    stringBuffer.append(TEXT_1278);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1279);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1280);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1281);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1282);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1283);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1284);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1285);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1286);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1287);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1288);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1289);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1290);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1291);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1292);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1293);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1294);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_1295);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1296);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1297);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1298);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1299);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1300);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1301);
    
									}

    							}else if(javaType == JavaTypesManager.BIGDECIMAL) {
									int pre = 10;
                       		 		Integer precision = column.getPrecision();
                           		 	if(precision!=null && precision!=0) { 
                           		 		pre = precision;
                           		 	}
                           		 	if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1302);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1303);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1304);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1305);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1306);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1307);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1308);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1309);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1310);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1311);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1312);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1313);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1314);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1315);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_1316);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1317);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1318);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1319);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1320);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1321);
    
                           		 	}else if(inJavaType == JavaTypesManager.BIGDECIMAL){

    stringBuffer.append(TEXT_1322);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1323);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1324);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1325);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1326);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1327);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1328);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1329);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1330);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1331);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1332);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1333);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1334);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1335);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1336);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1337);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1338);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_1339);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1340);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1341);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1342);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1343);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1344);
    
                           		 	}
								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT){
									if(inJavaType == JavaTypesManager.BYTE || inJavaType == JavaTypesManager.CHARACTER 
										|| inJavaType == JavaTypesManager.SHORT || inJavaType == JavaTypesManager.INTEGER 
										|| inJavaType == JavaTypesManager.LONG || inJavaType == JavaTypesManager.FLOAT 
										|| inJavaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1345);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1346);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1347);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1348);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1349);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1350);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1351);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1352);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1353);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1354);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1355);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1356);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1357);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1358);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1359);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1360);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1361);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1362);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1363);
    
									}else if(inJavaType == JavaTypesManager.BIGDECIMAL){
										int pre = 10;
                       		 			Integer precision = column.getPrecision();
                           		 		if(precision!=null && precision!=0) { 
                           		 			pre = precision;
                           		 		}

    stringBuffer.append(TEXT_1364);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1365);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1366);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1367);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1368);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1369);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1370);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1371);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1372);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1373);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1374);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1375);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1376);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1377);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1378);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1379);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1380);
    stringBuffer.append(pre );
    stringBuffer.append(TEXT_1381);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1382);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1383);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1384);
    stringBuffer.append(i );
    stringBuffer.append(TEXT_1385);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1386);
    
									}
								}else{

    stringBuffer.append(TEXT_1387);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1388);
    stringBuffer.append(JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable()) );
    stringBuffer.append(TEXT_1389);
    								}
							}else if(("distinct").equals(functions[i])){
								if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
									|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
									|| javaType == JavaTypesManager.LONG || javaType == JavaTypesManager.FLOAT 
									|| javaType == JavaTypesManager.DOUBLE){

    stringBuffer.append(TEXT_1390);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1391);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1392);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1393);
    stringBuffer.append(javaType.getPrimitiveClass() );
    stringBuffer.append(TEXT_1394);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1395);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1396);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1397);
    								}else if(javaType == JavaTypesManager.BIGDECIMAL) {

    stringBuffer.append(TEXT_1398);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1399);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1400);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1401);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1402);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1403);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1404);
    								}else if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_1405);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1406);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1407);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1408);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1409);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1410);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1411);
    								}else if(javaType == JavaTypesManager.BYTE_ARRAY) {

    stringBuffer.append(TEXT_1412);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1413);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1414);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1415);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1416);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1417);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1418);
    								}
							}else if(("list_object").equals(functions[i])){
								if(javaType == JavaTypesManager.STRING){

    stringBuffer.append(TEXT_1419);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1420);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1421);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1422);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1423);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1424);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1425);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1426);
    
								}else if(javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_1427);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1428);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1429);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1430);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1431);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1432);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1433);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1434);
    								}else if(javaType == JavaTypesManager.LIST) {

    stringBuffer.append(TEXT_1435);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1436);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1437);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1438);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1439);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1440);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1441);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1442);
    								}
							}else{//"list"
								if(javaType == JavaTypesManager.STRING || javaType == JavaTypesManager.OBJECT) {

    stringBuffer.append(TEXT_1443);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1444);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1445);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1446);
    stringBuffer.append(functions[i] );
    stringBuffer.append(TEXT_1447);
    stringBuffer.append(column_op[i][0].getLabel() );
    stringBuffer.append(TEXT_1448);
    stringBuffer.append(needTestForNull[i] );
    stringBuffer.append(TEXT_1449);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1450);
    								} else if(javaType == JavaTypesManager.LIST){

    stringBuffer.append(TEXT_1451);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1452);
    
								}else{

    stringBuffer.append(TEXT_1453);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1454);
    stringBuffer.append(JavaTypesManager.getTypeToGenerate(column.getTalendType(), column.isNullable()) );
    stringBuffer.append(TEXT_1455);
    								}
							}
							continue next_column;
						}
					}
				}
			}
		}
	}
	//do out end ...

	//??	

    stringBuffer.append(TEXT_1456);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1457);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1458);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1459);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1460);
    
conns = null;
conns = node.getOutgoingSortedConnections();
String firstConnName = "";
if (conns!=null) {
	for (int i=0;i<conns.size();i++) {
		IConnection conn = conns.get(i);
		if ((conn.getName().compareTo(firstConnName)!=0)&&(conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA))) {
			for (IMetadataColumn column: metadata.getListColumns()) {

    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_1461);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1462);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1463);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_1464);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_1465);
    			}
		}
	}
}

    stringBuffer.append(TEXT_1466);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_1467);
    
	}
	}
}

    return stringBuffer.toString();
  }
}
