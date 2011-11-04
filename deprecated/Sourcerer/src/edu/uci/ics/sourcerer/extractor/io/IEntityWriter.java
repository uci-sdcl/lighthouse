/*
* Sourcerer: an infrastructure for large-scale source code analysis.
* Copyright (C) by contributors. See CONTRIBUTORS.txt for full list.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package edu.uci.ics.sourcerer.extractor.io;

public interface IEntityWriter extends IExtractorWriter {
  public void writeClass(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeInterface(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeInitializer(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeConstructor(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeEnum(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeMethod(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeField(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeEnumConstant(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeAnnotation(String fqn, int modifiers, String filename, int startPos, int length);

  public void writeAnnotationMember(String fqn, int modifiers, String filename, int startPos, int length);
}