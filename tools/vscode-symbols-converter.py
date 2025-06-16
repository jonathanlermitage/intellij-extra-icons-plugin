#  SPDX-License-Identifier: MIT

########################################
# This script can be used to import https://github.com/miguelsolorio/vscode-symbols into Extra Icons
#
# Usage:
# 1. Install a reasonably recent version of Python 3
# 2. Check out or download a ZIP export (then unzip) of https://github.com/miguelsolorio/vscode-symbols. Let's say you extracted it to /home/jon/prj/vscode-symbols/
# 3. Consider you will generate a theme file /home/jon/prj/vscode-symbols-theme.json
# 4. Run: python vscode-symbols-converter.py /home/jon/prj/vscode-symbols/ /home/jon/prj/vscode-symbols-theme.json
# 5. You can now import the generated theme file. Go to IDE Settings > Appearance > Extra Icons > Import from file, then select the theme file
# 6. The theme is now imported in your IDE. You can see all the icons in the "User icons" table, in Extra Icons settings
########################################

import base64
import json
import os
import sys
from os.path import exists


def file_to_b64(file_path: str) -> str:
    with open(file_path, "r") as f:
        file_str = f.read()
        b64_bytes = base64.b64encode(file_str.encode("utf-8"))
        return b64_bytes.decode("utf-8")


def file_to_json(file_path: str) -> dict:
    with open(file_path, "r") as file:
        return json.loads(file.read())


if __name__ == '__main__':
    input_folder = sys.argv[1]
    output_file_path = sys.argv[2]
    print(f"Run on sources {input_folder}")

    in_theme_json = file_to_json(f"{input_folder}/src/symbol-icon-theme.json")
    icon_definitions = in_theme_json["iconDefinitions"]

    icon_name_and_path_dict = {}
    icon_path_and_b64_dict = {}
    for icon_name in icon_definitions:
        icon_path = str(icon_definitions[icon_name]["iconPath"]).replace("./", "")
        icon_path = f"{input_folder}/src/{icon_path}".replace("//", "/").strip()
        icon_name_and_path_dict[icon_name] = icon_path
        icon_path_and_b64_dict[icon_path] = file_to_b64(icon_path)
    print(f"Found {len(icon_name_and_path_dict)} icons")

    extension_name_and_icon_path_dict = {}
    file_extensions = in_theme_json["fileExtensions"]
    for file_extension in file_extensions:
        if file_extension not in file_extensions:
            continue
        if file_extensions[file_extension] not in icon_name_and_path_dict:
            continue
        extension_name_and_icon_path_dict[file_extension] = icon_name_and_path_dict[file_extensions[file_extension]]
    print(f"Found {len(extension_name_and_icon_path_dict)} file extension <-> icon associations")

    file_name_and_icon_path_dict = {}
    file_names = in_theme_json["fileNames"]
    for file_name in file_names:
        file_name_and_icon_path_dict[file_name] = icon_name_and_path_dict[file_names[file_name]]
    print(f"Found {len(file_name_and_icon_path_dict)} file name <-> icon associations")

    folder_name_and_icon_path_dict = {}
    folder_names = in_theme_json["folderNames"]
    for folder_name in folder_names:
        folder_name_and_icon_path_dict[folder_name] = icon_name_and_path_dict[folder_names[folder_name]]
    print(f"Found {len(folder_name_and_icon_path_dict)} folder name <-> icon associations")



    icon_path_and_file_names = {}
    for file_name in file_name_and_icon_path_dict:
        icon_path = file_name_and_icon_path_dict[file_name]
        if icon_path in icon_path_and_file_names:
            icon_path_and_file_names[icon_path].append(file_name)
        else:
            icon_path_and_file_names[icon_path] = [file_name]

    icon_path_and_folder_names = {}
    for folder_name in folder_name_and_icon_path_dict:
        icon_path = folder_name_and_icon_path_dict[folder_name]
        if icon_path in icon_path_and_folder_names:
            icon_path_and_folder_names[icon_path].append(folder_name)
        else:
            icon_path_and_folder_names[icon_path] = [folder_name]

    icon_path_and_extension_names = {}
    for extension_name in extension_name_and_icon_path_dict:
        icon_path = extension_name_and_icon_path_dict[extension_name]
        if icon_path in icon_path_and_extension_names:
            icon_path_and_extension_names[icon_path].append("." + extension_name)
        else:
            icon_path_and_extension_names[icon_path] = ["." + extension_name]



    model_idx = 1
    file_name_template = """\n\n{"icon":"{b64}","description":"{desc}","iconPack":"vscode-symbols","modelType":"FILE","iconType":"SVG","enabled":true,"conditions":[{"start":false,"eq":true,"mayEnd":false,"end":false,"noDot":false,"checkParent":false,"hasRegex":false,"enabled":true,"checkFacets":false,"hasIconEnabler":false,"isInProjectRootFolder":false,"hasContent":false,"names":["{names}"],"parentNames":[],"extensions":[],"facets":[]}],"autoLoadNewUIIconVariant":false}"""  # NOPEP8

    file_name_models = ""
    for icon_path in icon_path_and_file_names:
        if str(icon_path).endswith("files/cursor.svg") or str(icon_path).endswith("files/firebase.svg"):
            continue
        item = (file_name_template.replace("{b64}", icon_path_and_b64_dict[icon_path])
                .replace("{desc}", f"vscode-symbols-file-{model_idx}")
                .replace("{names}", "\",\"".join(icon_path_and_file_names[icon_path]))) + ",\n"
        file_name_models += item
        model_idx = model_idx + 1
    file_name_models = file_name_models[:-len(",\n")]  # remove the last separator



    model_idx = 1
    folder_name_template = """\n\n{"icon":"{b64}","description":"{desc}","iconPack":"vscode-symbols","modelType":"DIR","iconType":"SVG","enabled":true,"conditions":[{"start":false,"eq":true,"mayEnd":false,"end":false,"noDot":false,"checkParent":false,"hasRegex":false,"enabled":true,"checkFacets":false,"hasIconEnabler":false,"isInProjectRootFolder":false,"hasContent":false,"names":["{names}"],"parentNames":[],"extensions":[],"facets":[]}],"autoLoadNewUIIconVariant":false}"""  # NOPEP8

    folder_name_models = ""
    for icon_path in icon_path_and_folder_names:
        if str(icon_path).endswith("folders/folder-firebase.svg") or str(icon_path).endswith("folders/folder-cursor.svg"):
            continue
        item = (folder_name_template.replace("{b64}", icon_path_and_b64_dict[icon_path])
                .replace("{desc}", f"vscode-symbols-folder-{model_idx}")
                .replace("{names}", "\",\"".join(icon_path_and_folder_names[icon_path]))) + ",\n"
        folder_name_models += item
        model_idx = model_idx + 1
    folder_name_models = folder_name_models[:-len(",\n")]  # remove the last separator



    model_idx = 1
    extension_template = """\n\n{"icon":"{b64}","description":"{desc}","iconPack":"vscode-symbols","modelType":"FILE","iconType":"SVG","enabled":true,"conditions":[{"start":false,"eq":false,"mayEnd":false,"end":true,"noDot":false,"checkParent":false,"hasRegex":false,"enabled":true,"checkFacets":false,"hasIconEnabler":false,"isInProjectRootFolder":false,"hasContent":false,"names":[],"parentNames":[],"extensions":["{extensions}"],"facets":[]}],"autoLoadNewUIIconVariant":false}"""  # NOPEP8

    extension_name_models = ""
    for icon_path in icon_path_and_extension_names:
        item = (extension_template.replace("{b64}", icon_path_and_b64_dict[icon_path])
                .replace("{desc}", f"vscode-symbols-extension-{model_idx}")
                .replace("{extensions}", "\",\"".join(icon_path_and_extension_names[icon_path]))) + ",\n"
        extension_name_models += item
        model_idx = model_idx + 1
    extension_name_models = extension_name_models[:-len(",\n")]  # remove the last separator

    final_template = """{"name":"vscode-symbols","models":[\n{file_name_models},\n{folder_name_models},\n{extension_name_models}\n]}"""
    final_template = (final_template.replace("{file_name_models}", file_name_models)
                      .replace("{folder_name_models}", folder_name_models)
                      .replace("{extension_name_models}", extension_name_models))

    if exists(output_file_path):
        os.remove(output_file_path)
        print(f"Removed existing {output_file_path} file")
    with open(output_file_path, "w", newline="\n") as output_file:
        output_file.write(final_template)

    print(f"Generated {output_file_path} file with success")
