import os
with open('src/main/resources/templates/base.html', 'r') as f: lines = f.readlines()
with open('src/main/resources/templates/base.html', 'w') as f:
    for line in lines:
        if 'details' in line:
            f.write('    <details { 0f child.isParentOf(currentTagName) 