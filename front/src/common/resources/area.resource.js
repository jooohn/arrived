export default function AreaFactory($resource) {
  return $resource('/api/areas/:id', { id: '@id' })
}
