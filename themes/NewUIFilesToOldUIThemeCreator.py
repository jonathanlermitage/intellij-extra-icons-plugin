#  SPDX-License-Identifier: MIT
import base64
import glob
import os
import sys
from os.path import exists


def convert_icons_to_b64(icons: dict) -> dict:
    converted_icons = {}
    for icon in icons.keys():
        with open(icons[icon], "r") as f:
            file_str = f.read().encode('utf-8')
            encoded = base64.b64encode(file_str)
            converted_icons[icon] = encoded.decode("utf-8")
    print(f"✅  Converted {len(icons)} icons to Base64")
    return converted_icons  # icon paths with base64 pictures


def icon_pack_ij(icon_pack_items: list[str]) -> str:
    template = """
{
  "name":"NewUIFilesToOldUITheme",
  "models":[{icon_pack_items_str}]
}
"""
    icon_pack_items_str = ",".join(icon_pack_items)
    return template.replace("{icon_pack_items_str}", icon_pack_items_str)


def icon_pack_ij_item(icon_path, icon_b64) -> str:
    template = """
{"ideIcon": "{icon_path}", "icon": "{icon_b64}", "description": "{icon_path}", "iconPack": "", "modelType": "ICON", "iconType": "SVG", "enabled": true, "conditions": [{"start": false, "eq": false, "mayEnd": false, "end": false, "noDot": false, "checkParent": false, "hasRegex": false, "enabled": true, "checkFacets": false, "hasIconEnabler": false, "names": [], "parentNames": [], "extensions": [], "facets": []}]}
"""  # NOPEP8
    return template.replace("{icon_path}", icon_path).replace("{icon_b64}", icon_b64)


if __name__ == '__main__':
    ij_sources_folder_input = sys.argv[1]

    if not ij_sources_folder_input:
        raise ValueError("❌  IntelliJ sources folder required")
    if not exists(ij_sources_folder_input):
        raise ValueError(f"❌  IntelliJ sources folder '{ij_sources_folder_input}' not found")

    ij_sources_folder_input = ij_sources_folder_input.replace("\\", "/")

    # convert manually some new UI icon paths to old UI paths, because there is no exact matching
    path_substitutions = {
        "class.svg": "javaClass.svg",
        "expui/nodes": "modules",
    }

    # the new UI is a bit buggy: sometimes, we have to use the icon filename only, without the parent folder
    short_icon_name_fixes = {
        "/nodes/": ""
    }

    sub_folders_whitelist = ["fileTypes", "nodes"]  # todo exclude UI items

    print(f"✅  Loading all IJ SVG icons (old and new UI) from {ij_sources_folder_input}/platform/icons/")
    icon_pack = {}
    for file in glob.glob(f"{ij_sources_folder_input}/platform/icons/src/expui/**/*.svg"):
        file = file.replace("\\", "/")
        alt_file = None
        keep_icon = False
        for sub_folder_whitelisted in sub_folders_whitelist:
            if sub_folder_whitelisted in file:
                keep_icon = True
                break
        if not keep_icon:
            continue
        for icon_substitution in path_substitutions.keys():
            if icon_substitution in file:
                alt_file = file.replace(icon_substitution, path_substitutions[icon_substitution])
                break
        if exists(file.replace("/expui/", "/")):
            icon_pack[file.replace(f"{ij_sources_folder_input}/platform/icons/src/expui/", "/")] = file.replace("/expui/", "/")
        elif alt_file and exists(alt_file.replace("/expui/", "/")):
            icon_pack[file.replace(f"{ij_sources_folder_input}/platform/icons/src/expui/", "/")] = alt_file.replace("/expui/", "/")
    print(f"✅  Found {len(icon_pack)} valid icons for Icon Pack")

    icon_pack = convert_icons_to_b64(icon_pack)

    icon_pack_items = []
    for icon_name in icon_pack.keys():
        short_icon_name = icon_name.replace(f"{ij_sources_folder_input}/platform/icons/src", "")
        for short_icon_name_fixe in short_icon_name_fixes:
            if short_icon_name_fixe in short_icon_name:
                short_icon_name = short_icon_name.replace(short_icon_name_fixe, "")
                break
        icon_pack_items.append(icon_pack_ij_item(short_icon_name, icon_pack[icon_name]))

    json_icon_pack = icon_pack_ij(icon_pack_items)

    if exists("NewUIFilesToOldUITheme.json"):
        os.remove("NewUIFilesToOldUITheme.json")
        print(f"✅  Removed existing NewUIFilesToOldUITheme.json Icon Pack")
    with open("NewUIFilesToOldUITheme.json", "w") as json_icon_pack_file:
        json_icon_pack_file.write(json_icon_pack)
    print(f"✅  (★‿★) Created NewUIFilesToOldUITheme.json Icon Pack (★‿★)")
