# Changelog

{{#tags}}
## [{{name}}](https://github.com/xtexChooser/scp-sharp/compare/{{name}}) ({{tagDate .}})

{{#ifContainsType commits type='feat'}}
### Features

{{#commits}}
{{#ifCommitType . type='feat'}}
-{{#eachCommitScope .}} **{{.}}**{{/eachCommitScope}} {{{commitDescription .}}} ({{commitDate .}}, [{{hash}}](https://github.com/xtexChooser/scp-sharp/commit/{{hashFull}}) by {{authorName}})
{{/ifCommitType}}
{{/commits}}
{{/ifContainsType}}


{{#ifContainsType commits type='fix'}}
### Bug Fixes

{{#commits}}
{{#ifCommitType . type='fix'}}
-{{#eachCommitScope .}} **{{.}}**{{/eachCommitScope}} {{{commitDescription .}}} ({{commitDate .}}, [{{hash}}](https://github.com/xtexChooser/scp-sharp/commit/{{hashFull}}) by {{authorName}})
{{/ifCommitType}}
{{/commits}}
{{/ifContainsType}}

### Other

{{#commits}}
{{#ifCommitTypeOtherThan . type="feat"}}
{{#ifCommitTypeOtherThan . type="fix"}}
- [{{message}}](https://github.com/xtexChooser/scp-sharp/commit/{{hashFull}}) ({{commitDate .}} by {{authorName}})
{{/ifCommitTypeOtherThan}}
{{/ifCommitTypeOtherThan}}
{{/commits}}

{{/tags}}
