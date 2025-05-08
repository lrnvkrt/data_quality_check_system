import { useNotificationContext } from '../components/ui/NotificationProvider'

export const useNotification = () => {
    const { showNotification } = useNotificationContext()
    return showNotification
}