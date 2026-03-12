{{- define "demo-app.labels" -}}
app: {{ include "demo-app.name" . }}
{{- end -}}

{{- define "demo-app.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "demo-app.fullname" -}}
{{- if .Values.fullnameOverride -}}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" -}}
{{- else -}}
{{- printf "%s" (include "demo-app.name" .) | trunc 63 | trimSuffix "-" -}}
{{- end -}}
{{- end -}}
