#  SPDX-License-Identifier: MIT
import base64
import glob
import hashlib
import json
import os
import re
import subprocess
import sys
from os.path import exists
from typing import Tuple

NEUTRAL = " ⚪ "
OK = " 🟢 "
NEW = " 🔵 "
ERR = " 🔴 "


def convert_icons_to_b64(icons: dict) -> dict:
    """
    Convert a list of icons path to Base64 icons.

    :param icons: a dictionary with icons name as keys and icons absolute path as values
    :return: a dictionary with icons name as keys and Base64 icons as values
    """
    converted_icons = {}
    for icon in icons.keys():
        with open(icons[icon], "r") as f:
            file_str = f.read().encode('utf-8')
            encoded = base64.b64encode(file_str)
            converted_icons[icon] = encoded.decode("utf-8")
    print(f"{OK}Converted {len(icons)} icons to Base64")
    return converted_icons  # icon paths with base64 pictures


def json_icon_pack_ij(items: list[str], version: int) -> str:
    """
    Generate a JSON IconPack string for given list of icons and version number.

    :param items: icons
    :param version: version number
    :return: JSON IconPack string
    """
    template = """{"name": "NewUIFilesToOldUITheme_v{icon_pack_version}","models": [
{icon_pack_items_str}
]}
"""
    icon_pack_items_str = ",\n".join(items)
    return template.replace("{icon_pack_items_str}", icon_pack_items_str) \
        .replace("{icon_pack_version}", str(version)) \
        .replace(", ", ",") \
        .replace(": ", ":")


def md5_sum_from_file(file_path: str) -> str:
    """
    Compute MD5 string for given file.
    """
    if exists:
        file_hash = hashlib.md5()
        with open(file_path, "rb") as f:
            for chunk in iter(lambda: f.read(128 * file_hash.block_size), b""):
                file_hash.update(chunk)
        return file_hash.hexdigest()
    return ""


def md5_sum_from_str(content: str):
    """
    Compute MD5 string for given text string.
    """
    m = hashlib.md5()
    m.update(content.encode("utf-8"))
    return m.hexdigest()


def icon_pack_ij_item(icon_path, icon_b64) -> str:
    """
    Generate a JSON IconPack item string for given icon name and Base64 icon.

    :param icon_path: icon's path
    :param icon_b64:  icon as Base64
    :return: a JSON IconPack item
    """
    template = """{"ideIcon": "{icon_path}", "icon": "{icon_b64}", "description": "{icon_path}", "iconPack": "", "modelType": "ICON", "iconType": "SVG", "enabled": true, "conditions": [{"start": false, "eq": false, "mayEnd": false, "end": false, "noDot": false, "checkParent": false, "hasRegex": false, "enabled": true, "checkFacets": false, "hasIconEnabler": false, "names": [], "parentNames": [], "extensions": [], "facets": []}]}"""  # NOPEP8
    return template.replace("{icon_path}", icon_path).replace("{icon_b64}", icon_b64)


def get_icon_pack_version_and_nb_icons_from_file(icon_pack_path) -> Tuple[int, int]:
    with open(icon_pack_path, "r") as f:
        json_data = json.loads(f.read())
        return int(json_data["name"].replace("NewUIFilesToOldUITheme_v", "")), len(json_data["models"])


if __name__ == '__main__':
    ij_sources_folder_input = sys.argv[1]

    if not ij_sources_folder_input:
        raise ValueError(f"{ERR}IntelliJ sources folder required")
    if not exists(ij_sources_folder_input):
        raise ValueError(f"{ERR}IntelliJ sources folder '{ij_sources_folder_input}' not found")
    if not exists("NewUIFilesToOldUITheme.json"):
        raise FileNotFoundError(f"{ERR}Can't find NewUIFilesToOldUITheme.json")

    print(f"{NEUTRAL}Run git pull on IJ sources {ij_sources_folder_input}:", end=" ")
    ij_sources_pull_call = subprocess.run(f"git -C {ij_sources_folder_input} pull", shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    git_pull_result = ij_sources_pull_call.stdout.decode("utf-8")
    if git_pull_result.endswith("\n"):
        git_pull_result = git_pull_result[:-1]
    print(f"\x1b[0;34m{git_pull_result}\x1b[0m")

    # we reset the theme's file because we want to be able to compute all the changes since last commit, even
    # if we run this Python program multiple times
    restore_theme_call = subprocess.run("git restore NewUIFilesToOldUITheme.json", shell=True)
    if restore_theme_call.returncode == 0:
        print(f"{OK}Restored NewUIFilesToOldUITheme.json file with success")
    else:
        raise ValueError(
            f"{ERR}Failed to restore NewUIFilesToOldUITheme.json file ; "
            f"command returned code {restore_theme_call.returncode}")

    icon_pack_version, nb_icons = get_icon_pack_version_and_nb_icons_from_file("NewUIFilesToOldUITheme.json")

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

    sub_folders_whitelist = ["fileTypes", "nodes"]

    # find icons to include in IconPack
    print(f"{OK}Loading all IJ SVG icons (old and new UI) from {ij_sources_folder_input}/platform/icons/")
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
    print(f"{OK}Found {len(icon_pack)} valid icons for Icon Pack")

    icon_pack = convert_icons_to_b64(icon_pack)

    # compute icon names we will use in IconPack
    icon_pack_items = []
    for icon_name in icon_pack.keys():
        short_icon_name = icon_name.replace(f"{ij_sources_folder_input}/platform/icons/src", "")
        for short_icon_name_fixe in short_icon_name_fixes:
            if short_icon_name_fixe in short_icon_name:
                short_icon_name = short_icon_name.replace(short_icon_name_fixe, "")
                break
        icon_pack_items.append(icon_pack_ij_item(short_icon_name, icon_pack[icon_name]))

    # set IconPack new version
    json_icon_pack = json_icon_pack_ij(icon_pack_items, icon_pack_version)
    old_md5 = md5_sum_from_file("NewUIFilesToOldUITheme.json")
    json_icon_pack_updated = (old_md5 != md5_sum_from_str(json_icon_pack))
    if json_icon_pack_updated:
        new_icon_pack_version = icon_pack_version + 1
        json_icon_pack = json_icon_pack.replace(
            f"NewUIFilesToOldUITheme_v{icon_pack_version}",
            f"NewUIFilesToOldUITheme_v{new_icon_pack_version}")

    # write the new IconPack
    os.remove("NewUIFilesToOldUITheme.json")
    with open("NewUIFilesToOldUITheme.json", "w", newline="\n") as json_icon_pack_file:
        json_icon_pack_file.write(json_icon_pack)
    print(f"{OK}つ ◕_◕ ༽つ Created NewUIFilesToOldUITheme.json Icon Pack")

    # if IconPack has been updated, say it, update THEMES.md with the new number of icons in IconPack,
    # and update JSON IconPack file's version
    if json_icon_pack_updated:
        print(f"{NEW}つ ◕_◕ ༽つ NewUIFilesToOldUITheme.json has been updated!")

        version_tag_start = "<!--NewUIFilesToOldUITheme_nbOfIcons_start-->"
        version_tag_end = "<!--NewUIFilesToOldUITheme_nbOfIcons_end-->"

        version_regex = rf"{version_tag_start}[0-9]+{version_tag_end}"
        version_replacement = f"{version_tag_start}{len(icon_pack)}{version_tag_end}"

        with open("THEMES.md", "r") as theme_md_file:
            themes_md_str = re.sub(version_regex, version_replacement, str(theme_md_file.read()))

        with open("THEMES.md", "w", newline="\n") as theme_md_file:
            theme_md_file.write(themes_md_str)
            print(f"{NEW}つ ◕_◕ ༽つ Updated THEMES.md with the new number of icons "
                  f"({len(icon_pack)}, \x1b[1;34m{len(icon_pack) - nb_icons} new\x1b[0m)")
            print(f"{NEUTRAL}Git diff on NewUIFilesToOldUITheme.json:")
            git_diff_call = subprocess.run("git diff NewUIFilesToOldUITheme.json", shell=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
            git_result = git_diff_call.stdout.decode("utf-8")
            git_result = re.sub("icon\":\".+\",", "icon:\"(base64 content)\",", git_result)
            for line in git_result.split("\n"):
                if line.startswith("---") or line.startswith("+++"):
                    continue
                if len(line) > 120:
                    line = line[:119] + "…"
                if line.startswith("-"):
                    print(f"\x1b[31m{line}\x1b[0m")
                if line.startswith("+"):
                    print(f"\x1b[32m{line}\x1b[0m")
