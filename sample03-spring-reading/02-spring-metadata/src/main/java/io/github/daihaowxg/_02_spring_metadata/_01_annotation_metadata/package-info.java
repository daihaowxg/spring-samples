/**
 * 第 1 阶段：AnnotationMetadata (元数据读取核心)
 * <p>
 * 本包演示了无需加载类即可读取类元数据的能力，这是 Spring 扫描机制的基础。
 * <p>
 * <b>📚 阅读顺序：</b>
 * <ol>
 *   <li>{@link io.github.daihaowxg._02_spring_metadata._01_annotation_metadata.AnnotationMetadataReflectionDemo
 *       AnnotationMetadataReflectionDemo}
 *       - 基于反射的标准元数据读取（Spring 早期方式）</li>
 *   <li>{@link io.github.daihaowxg._02_spring_metadata._01_annotation_metadata.AnnotationMetadataAsmDemo
 *       AnnotationMetadataAsmDemo}
 *       - 基于 ASM 的元数据读取（Spring 核心方式，无需加载类）</li>
 * </ol>
 *
 * @see io.github.daihaowxg._02_spring_metadata._02_metadata_reader 下一阶段：MetadataReader 统一接口
 */
package io.github.daihaowxg._02_spring_metadata._01_annotation_metadata;