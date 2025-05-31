import './App.css'
import { Button, ThemeProvider } from '@mui/material'
import redTheme from './theme/redTheme'
function App() {

  return (
    <ThemeProvider theme={redTheme}>
      <div className="px-100">  
        <Button variant="contained" color="primary">
          vivek
        </Button>
      </div>
    </ThemeProvider>
  )
}

export default App
